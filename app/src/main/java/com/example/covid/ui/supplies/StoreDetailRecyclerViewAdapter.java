package com.example.covid.ui.supplies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid.R;
import com.example.covid.data.Supply;

import java.util.ArrayList;
import java.util.Map;

public class StoreDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static class StoreDetailViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView typeName;
        public final RecyclerView supplydetail;

        public StoreDetailViewHolder(View view)
        {
            super(view);
            typeName = (TextView) itemView.findViewById(R.id.supplydetail_SupplyType);
            supplydetail = (RecyclerView) itemView.findViewById(R.id.supplydetail_RV);
        }
    }

    private Map<String, ArrayList> supplies;
    private ArrayList<String> supplyTypes = new ArrayList<>();

    public StoreDetailRecyclerViewAdapter(Map<String, ArrayList> supplies, Context context)
    {
        this.supplies = supplies;

        for(String type : supplies.keySet())
        {
            if (supplies.get(type).isEmpty()) continue;
            supplyTypes.add(type);
        }

        if(supplyTypes.isEmpty())
        {
            StoreDetailActivity activity = (StoreDetailActivity) context;
            TextView title = (TextView) activity.findViewById(R.id.storedetail_TitleAS);
            title.setText("No supply available now");
            title.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {return supplyTypes.size();}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_storedetail_recyclerview, viewGroup, false);
        StoreDetailViewHolder viewHolder = new StoreDetailViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
    {
        final StoreDetailViewHolder storeDetailViewHolder = (StoreDetailViewHolder) viewHolder;

        switch(supplyTypes.get(i))
        {
            case "SURGICAL_MASK":
                storeDetailViewHolder.typeName.setText("Surgical Mask");
                break;
            case "HAND_SANITIZER":
                storeDetailViewHolder.typeName.setText("Hand Sanitizer");
                break;
            case "BLEACH":
                storeDetailViewHolder.typeName.setText("Bleach");
                break;
            case "RUBBING_ALCOHOL":
                storeDetailViewHolder.typeName.setText("Rubbing Alcohol");
                break;
            case "RESPIRATOR":
                storeDetailViewHolder.typeName.setText("Respirator");
                break;
            case "ISOLATION_CLOTHING":
                storeDetailViewHolder.typeName.setText("Isolation Clothing");
                break;
        }

        storeDetailViewHolder.supplydetail.setLayoutManager(new LinearLayoutManager(storeDetailViewHolder.supplydetail.getContext()));
        storeDetailViewHolder.supplydetail.setAdapter(new SupplyDetailRecyclerViewAdapter(supplies.get(supplyTypes.get(i))));

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
