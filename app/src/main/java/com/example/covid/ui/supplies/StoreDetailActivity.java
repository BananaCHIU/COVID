package com.example.covid.ui.supplies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covid.R;
import com.example.covid.data.Store;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
        final TextView businessHour = (TextView) findViewById((R.id.storedetail_BH));
        final TextView address = (TextView) findViewById(R.id.storedetail_Address);

        final FloatingActionButton btn_addSupply = (FloatingActionButton) findViewById(R.id.btn_addSupply);
        btn_addSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreDetailActivity.this, AddSupplyActivity.class);
                intent.putExtra("storeID", store.getStoreID());
                //TODO: IDK Why the supplies in the store is null
                intent.putExtra("supplies", supply);
                startActivityForResult(intent, 1);
            }
        });

        Picasso.get().load(store.getImageURL())
                .placeholder(R.drawable.placeholder)
                .transform(new CropSquareTransformation())
                .transform(new RoundedCornersTransformation(200,0)).into(image);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + store.getStoreName() + "</font>"));
        businessHour.setText(store.getTimeOpen() + " - " + store.getTimeClose());
        address.setText(store.getAddress() + ", " + store.getDistrict());

        RecyclerView storeDetail = (RecyclerView) findViewById(R.id.storedetail_RV);
        storeDetail.setLayoutManager(new LinearLayoutManager(this));
        storeDetail.setNestedScrollingEnabled(false);
        storeDetail.setAdapter(new StoreDetailRecyclerViewAdapter(supply, this));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            finish();
        }
    }

}
