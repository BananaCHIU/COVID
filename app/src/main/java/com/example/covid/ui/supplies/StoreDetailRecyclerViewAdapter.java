package com.example.covid.ui.supplies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public StoreDetailRecyclerViewAdapter(Map<String, ArrayList> supplies)
    {
        this.supplies = supplies;
        supplyTypes.addAll(supplies.keySet());
    }

    @Override
    public int getItemCount() {return supplies.size();}

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
        storeDetailViewHolder.typeName.setText(supplyTypes.get(i));

        storeDetailViewHolder.supplydetail.setLayoutManager(new LinearLayoutManager(storeDetailViewHolder.supplydetail.getContext()));
        storeDetailViewHolder.supplydetail.setAdapter(new SupplyDetailRecyclerViewAdapter(supplies.get(supplyTypes.get(i))));

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
