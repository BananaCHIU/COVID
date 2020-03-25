package com.example.covid.ui.supplies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.covid.R;

public class AddSupplyActivity extends AppCompatActivity {

    String storeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply);
        getSupportActionBar().hide();
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
            }
        });
    }
}
