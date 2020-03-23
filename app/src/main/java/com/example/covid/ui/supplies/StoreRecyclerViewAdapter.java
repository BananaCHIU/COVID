package com.example.covid.ui.supplies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid.R;
import com.example.covid.data.Store;
import com.example.covid.data.Supply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static class StoreViewHolder extends RecyclerView.ViewHolder
    {
        public final CardView storeCardView;
        public final ImageView storeImage;
        public final TextView storeName;
        public final TextView maskType;
        public final TextView handType;
        public final TextView bleachType;
        public final TextView alcoholType;
        public final TextView respiratorType;
        public final TextView clothingType;


        public StoreViewHolder(View view)
        {
            super(view);
            storeCardView = (CardView)itemView.findViewById(R.id.store_CardView);
            storeImage = (ImageView)itemView.findViewById(R.id.store_Image);
            storeName = (TextView) itemView.findViewById(R.id.store_Name);
            maskType = (TextView) itemView.findViewById(R.id.store_TypeMask);
            handType = (TextView) itemView.findViewById(R.id.store_TypeHS);
            bleachType = (TextView) itemView.findViewById(R.id.store_TypeBleach);
            alcoholType = (TextView) itemView.findViewById(R.id.store_TypeRA);
            respiratorType = (TextView) itemView.findViewById(R.id.store_TypeRespirator);
            clothingType = (TextView) itemView.findViewById(R.id.store_TypeIC);
        }
    }

    private ArrayList<Store> stores;

    public StoreRecyclerViewAdapter(ArrayList<Store> stores)
    {
        this.stores = stores;
    }

    @Override
    public int getItemCount() {return stores.size();}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_supplies_recyclerview, viewGroup, false);
        StoreViewHolder viewHolder = new StoreViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
    {
        final StoreViewHolder storeViewHolder = (StoreViewHolder)viewHolder;
        //storeViewHolder.storeImage.setImageBitmap(stores.get(i).getImage());
        storeViewHolder.storeName.setText(stores.get(i).getStoreName());

        ArrayList<String> sType = new ArrayList<>();
        sType.addAll(stores.get(i).getSupplies().keySet());

        for(String type : sType)
        {
            switch(type)
            {
                case "SURGICAL_MASK":
                    storeViewHolder.maskType.setTextColor(Color.GREEN);
                    break;
                case "HAND_SANITIZER":
                    storeViewHolder.handType.setTextColor(Color.GREEN);
                    break;
                case "BLEACH":
                    storeViewHolder.bleachType.setTextColor(Color.GREEN);
                    break;
                case "RUBBING_ALCOHOL":
                    storeViewHolder.alcoholType.setTextColor(Color.GREEN);
                    break;
                case "RESPIRATOR":
                    storeViewHolder.respiratorType.setTextColor(Color.GREEN);
                    break;
                case "ISOLATION_CLOTHING":
                    storeViewHolder.clothingType.setTextColor(Color.GREEN);
                    break;
            }
        }


        storeViewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), StoreDetailActivity.class);
                Store temp = stores.get(i);
                HashMap<String, ArrayList> supply = (HashMap<String, ArrayList>) temp.getSupplies();
                intent.putExtra("store", temp);
                intent.putExtra("supply", supply);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
