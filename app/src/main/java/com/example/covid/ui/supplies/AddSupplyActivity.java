package com.example.covid.ui.supplies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covid.R;
import com.example.covid.data.Supply;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSupplyActivity extends AppCompatActivity {

    private static final String TAG = "AddSupplyActivity";
    private String storeID;
    private HashMap<String, ArrayList> supplies;
    private ArrayList<Integer> pos = new ArrayList<>();

    private ChipGroup chip_type;
    private TextInputLayout[] input_name = new TextInputLayout[3], input_price = new TextInputLayout[3];
    private ProgressBar progress_add_supplies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply);
        getSupportActionBar().hide();

        progress_add_supplies = findViewById(R.id.progress_add_supplies);
        chip_type = findViewById(R.id.chip_type);
        input_name[0] = findViewById(R.id.input_supplyName);
        input_price[0] = findViewById(R.id.input_supplyPrice);
        input_name[1] = findViewById(R.id.input_supplyName1);
        input_price[1] = findViewById(R.id.input_supplyPrice1);
        input_name[2] = findViewById(R.id.input_supplyName2);
        input_price[2] = findViewById(R.id.input_supplyPrice2);


        Intent intent = getIntent();
        storeID = intent.getStringExtra("storeID");
        supplies = (HashMap<String, ArrayList>) intent.getSerializableExtra("supplies");

        final ImageButton btn_close = findViewById(R.id.btn_close_add_supplies);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button btn_add = findViewById(R.id.btn_add_supplies);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Upload data
                if (isValidInfo()){
                    progress_add_supplies.setVisibility(View.VISIBLE);
                    uploadData();
                }
            }
        });
    }

    private boolean isValidInfo(){
        boolean valid = true;
        if(chip_type.getCheckedChipId() == -1){
            Toast.makeText(this, "Please select the supply type.", Toast.LENGTH_LONG).show();
            valid = false;
        }
        for(int i = 0; i < 3; ++i) {
            String name = input_name[i].getEditText().getText().toString();
            String price = input_price[i].getEditText().getText().toString();
            if (TextUtils.isEmpty(name)) {
                if (i == 0 || !TextUtils.isEmpty(price)){
                    input_name[i].setError("Required.");
                    valid = false;
                }else input_name[i].setError(null);
            } else {
                input_name[i].setError(null);
                if (TextUtils.isEmpty(price)) {
                    input_price[i].setError("Required.");
                    valid = false;

                } else if (Double.parseDouble(price) < 0) {
                    input_price[i].setError("Please enter a non-negative number.");
                    valid = false;
                } else {
                    input_price[i].setError(null);
                    pos.add(i);
                }
            }

        }
        return valid;
    }

    private void uploadData(){
        Supply.suppliesType supplyType = null;
        switch (chip_type.getCheckedChipId()){
            case R.id.add_alcohol:
                supplyType = Supply.suppliesType.RUBBING_ALCOHOL;
                break;
            case R.id.add_bleach:
                supplyType = Supply.suppliesType.BLEACH;
                break;
            case R.id.add_cloth:
                supplyType = Supply.suppliesType.ISOLATION_CLOTHING;
                break;
            case R.id.add_hand_san:
                supplyType = Supply.suppliesType.HAND_SANITIZER;
                break;
            case R.id.add_mask:
                supplyType = Supply.suppliesType.SURGICAL_MASK;
                break;
            case R.id.add_respirator:
                supplyType = Supply.suppliesType.RESPIRATOR;
                break;
        }
        for(int i = 0; i < pos.size(); ++i){
            ArrayList<Supply> suppliesOfType = (ArrayList<Supply>) supplies.remove(supplyType.toString());
            suppliesOfType.add(
                    new Supply(supplyType, input_name[pos.get(i)].getEditText().getText().toString()
                            , Double.parseDouble(input_price[pos.get(i)].getEditText().getText().toString())));
            supplies.put(supplyType.toString(), suppliesOfType);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference storeRef = db.collection("stores").document(storeID);

        storeRef.update("supplies", supplies)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        progress_add_supplies.setVisibility(View.GONE);
                        setResult(2);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        Toast.makeText(AddSupplyActivity.this, "Upload Failed.", Toast.LENGTH_LONG);
                    }
                });


    }

}
