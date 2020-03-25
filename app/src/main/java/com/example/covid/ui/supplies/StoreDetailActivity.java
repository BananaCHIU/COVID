package com.example.covid.ui.supplies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covid.R;
import com.example.covid.data.Store;

import java.util.HashMap;
import java.util.ArrayList;

public class StoreDetailActivity extends AppCompatActivity {

    private Store store;
    private HashMap<String, ArrayList> supply;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        store = (Store) this.getIntent().getParcelableExtra("store");
        supply = (HashMap<String, ArrayList>) this.getIntent().getSerializableExtra("supply");

        final ImageView image = (ImageView) findViewById(R.id.storedetail_Image);
        final TextView name = (TextView) findViewById(R.id.storedetail_Name);
        final TextView businessHour = (TextView) findViewById((R.id.storedetail_BH));
        final TextView address = (TextView) findViewById(R.id.storedetail_Address);

        //image.setImageBitmap(store.getImage());
        name.setText(store.getStoreName());
        getSupportActionBar().setTitle(store.getStoreName());
        businessHour.setText(store.getTimeOpen() + " - " + store.getTimeClose());
        address.setText(store.getAddress());

        RecyclerView storeDetail = (RecyclerView) findViewById(R.id.storedetail_RV);
        storeDetail.setLayoutManager(new LinearLayoutManager(this));
        storeDetail.setNestedScrollingEnabled(false);
        storeDetail.setAdapter(new StoreDetailRecyclerViewAdapter(supply));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
