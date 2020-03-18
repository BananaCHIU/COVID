package com.example.covid.ui.supplies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.covid.R;
import com.example.covid.data.Store;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuppliesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuppliesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        View view = inflater.inflate(R.layout.fragment_supplies, container, false);
        List<Store> stores = Store.sample_data();

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.store_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new StoreRecyclerViewAdapter(stores));
        if(stores.size() == 0)
        {
            Toast.makeText(getActivity(), "No store recorded", Toast.LENGTH_LONG).show();
        }

        return view;
    }
}
