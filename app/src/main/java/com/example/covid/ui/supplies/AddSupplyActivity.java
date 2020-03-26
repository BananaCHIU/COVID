package com.example.covid.ui.supplies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covid.R;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class AddSupplyActivity extends AppCompatActivity {

    private String storeID;
    private ChipGroup chip_type;
    private TextInputLayout[] input_name = new TextInputLayout[3], input_price = new TextInputLayout[3];
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply);
        getSupportActionBar().hide();

        chip_type = findViewById(R.id.chip_type);
        input_name[0] = findViewById(R.id.input_supplyName);
        input_price[0] = findViewById(R.id.input_supplyPrice);
        input_name[1] = findViewById(R.id.input_supplyName1);
        input_price[1] = findViewById(R.id.input_supplyPrice1);
        input_name[2] = findViewById(R.id.input_supplyName2);
        input_price[2] = findViewById(R.id.input_supplyPrice2);


        Intent intent = getIntent();
        storeID = intent.getStringExtra("storeID");

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

                }
            }
        });
    }

    private boolean isValidInfo(){
        boolean valid = true;
        count = 0;
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
                    ++count;
                }
            }

        }
        return valid;
    }

}
