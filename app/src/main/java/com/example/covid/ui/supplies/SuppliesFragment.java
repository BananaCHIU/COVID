package com.example.covid.ui.supplies;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covid.R;
import com.example.covid.data.Store;
import com.example.covid.data.Supply;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.perf.metrics.AddTrace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.google.firebase.crashlytics.internal.Logger.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuppliesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuppliesFragment extends Fragment {

    private FloatingActionButton btn_addStore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NUM_SUPPLY = 6;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private View view;
    private Spinner spinner;
    private ArrayList<Store> stores;
    private Map<String, Boolean> filterSupply;
    private Chip[] chips;
    private ChipGroup chipGroup;

    public SuppliesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuppliesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuppliesFragment newInstance(String param1, String param2) {
        SuppliesFragment fragment = new SuppliesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_supplies, container, false);

        initFilterSupply();


        recyclerView = (RecyclerView) view.findViewById(R.id.store_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.district, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String area = parentView.getItemAtPosition(position).toString();
                if(area.contains("- -")){
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Please select a valid district.", Snackbar.LENGTH_LONG).show();
                }else{
                    filterDataWithArea(area);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        btn_addStore = view.findViewById(R.id.btn_addStore);
        btn_addStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddStoreActivity.class);
                startActivity(intent);
            }
        });
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.primaryDarkColor);
        mySwipeRefreshLayout.canChildScrollUp();
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        String area = spinner.getSelectedItem().toString();
                        if (!area.contains("- -")){
                            downloadDataWithAreaFilter(area);
                        }else {
                            Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    "Please select a valid district.", Snackbar.LENGTH_LONG).show();
                            mySwipeRefreshLayout.setRefreshing(false);
                        }

                    }
                }
        );
        mySwipeRefreshLayout.setRefreshing(true);
        downloadStoreData();
        return view;
    }

    @AddTrace(name = "DownloadStoreDataTrace", enabled = true)
    private void downloadStoreData(){
        mySwipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("stores")
                .whereEqualTo("approved", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stores = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                Store store = new Store();
                                store.setImage(document.getString("imageURL"));
                                store.setAddress(document.getString("address"));
                                store.setDistrict(document.getString("district"));
                                store.setStoreID(document.getString("id"));
                                store.setStoreName(document.getString("name"));
                                store.setSupplies((Map<String, ArrayList>) document.get("supplies"));
                                store.setTimeOpen(document.getString("timeOpen"));
                                store.setTimeClose(document.getString("timeClose"));
                                stores.add(store);
                            }
                            filterStoreWithSupply(stores);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @AddTrace(name = "FilterDataWithAreaTrace", enabled = true)
    private void filterDataWithArea(String area){
        mySwipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("stores")
                .whereEqualTo("district", area)
                .whereEqualTo("approved", true)
                .get(Source.CACHE)                                              //Get data locally
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stores = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                Store store = new Store();
                                store.setImage(document.getString("imageURL"));
                                store.setAddress(document.getString("address"));
                                store.setDistrict(document.getString("district"));
                                store.setStoreID(document.getString("id"));
                                store.setStoreName(document.getString("name"));
                                store.setSupplies((Map<String, ArrayList>) document.get("supplies"));
                                store.setTimeOpen(document.getString("timeOpen"));
                                store.setTimeClose(document.getString("timeClose"));
                                stores.add(store);
                            }
                            filterStoreWithSupply(stores);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @AddTrace(name = "DownloadDataWithAreaFilterTrace", enabled = true)
    private void downloadDataWithAreaFilter(String area){
        mySwipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("stores")
                .whereEqualTo("district", area)
                .whereEqualTo("approved", true)
                .get()                                              //Get data locally
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            stores = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                Store store = new Store();
                                store.setImage(document.getString("imageURL"));
                                store.setAddress(document.getString("address"));
                                store.setDistrict(document.getString("district"));
                                store.setStoreID(document.getString("id"));
                                store.setStoreName(document.getString("name"));
                                store.setSupplies((Map<String, ArrayList>) document.get("supplies"));
                                store.setTimeOpen(document.getString("timeOpen"));
                                store.setTimeClose(document.getString("timeClose"));
                                stores.add(store);
                            }
                            filterStoreWithSupply(stores);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void filterStoreWithSupply(ArrayList<Store> stores){

        if (filterSupply.containsValue(true)) {
            ArrayList<Store> filteredStores = new ArrayList<>();
            filterSupply.forEach((s, aBoolean) -> {
                if(aBoolean){
                    for (int i = 0; i < stores.size(); ++i){
                        if(!filteredStores.contains(stores.get(i)) && stores.get(i).getSupplies().get(s).size() != 0){
                            filteredStores.add(stores.get(i));
                        }
                    }
                }
            });
            recyclerView.setAdapter(new StoreRecyclerViewAdapter(filteredStores));
            if (filteredStores.size() == 0){
                Toast.makeText(getActivity(), "No store",
                        Toast.LENGTH_LONG).show();
            }
        } else{
            recyclerView.setAdapter(new StoreRecyclerViewAdapter(stores));
            if (stores.size() == 0){
                Toast.makeText(getActivity(), "No store",
                        Toast.LENGTH_LONG).show();
            }
        }

        mySwipeRefreshLayout.setRefreshing(false);
    }

    private void initFilterSupply(){
        filterSupply = new HashMap<>();
        filterSupply.put(Supply.suppliesType.SURGICAL_MASK.toString(), false);
        filterSupply.put(Supply.suppliesType.HAND_SANITIZER.toString(), false);
        filterSupply.put(Supply.suppliesType.BLEACH.toString(), false);
        filterSupply.put(Supply.suppliesType.RUBBING_ALCOHOL.toString(), false);
        filterSupply.put(Supply.suppliesType.RESPIRATOR.toString(), false);
        filterSupply.put(Supply.suppliesType.ISOLATION_CLOTHING.toString(), false);

        chips = new Chip[6];
        chips[0] = view.findViewById(R.id.mask);
        chips[1] = view.findViewById(R.id.hand_san);
        chips[2] = view.findViewById(R.id.bleach);
        chips[3] = view.findViewById(R.id.alcohol);
        chips[4] = view.findViewById(R.id.respirator);
        chips[5] = view.findViewById(R.id.cloth);
        chipGroup = view.findViewById(R.id.chip_type_store);
        for (int i = 0; i < NUM_SUPPLY; ++i){
            chips[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    filterSupply.replace(compoundButton.getTag().toString(), b);
                    filterStoreWithSupply(stores);
                }
            });
        }
    }



    @Override
    public void onResume() {
        //downloadStoreData();
        super.onResume();

    }
}
