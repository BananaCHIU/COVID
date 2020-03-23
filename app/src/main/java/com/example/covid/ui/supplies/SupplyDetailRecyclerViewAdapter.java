package com.example.covid.ui.supplies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.covid.R;
import com.example.covid.data.Supply;

import java.util.ArrayList;
import java.util.Map;

public class SupplyDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static class SupplyDetailViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView brandName;
        public final TextView price;

        public SupplyDetailViewHolder(View view)
        {
            super(view);
            brandName = (TextView) itemView.findViewById(R.id.supplydetail_BrandName);
            price = (TextView) itemView.findViewById(R.id.supplydetail_Price);
        }
    }

    private ArrayList<Supply> suppliesDetail;

    public SupplyDetailRecyclerViewAdapter(ArrayList<Supply> suppliesDetail)
    {
        this.suppliesDetail = suppliesDetail;
    }

    @Override
    public int getItemCount() {return suppliesDetail.size();}

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_supplydetail_recyclerview, viewGroup, false);
        SupplyDetailViewHolder viewHolder = new SupplyDetailViewHolder(v);
        return viewHolder;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i)
    {
        final SupplyDetailViewHolder supplyDetailViewHolder = (SupplyDetailViewHolder) viewHolder;
        supplyDetailViewHolder.brandName.setText(suppliesDetail.get(i).getName());
        supplyDetailViewHolder.price.setText(Double.toString(suppliesDetail.get(i).getPrice()));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
