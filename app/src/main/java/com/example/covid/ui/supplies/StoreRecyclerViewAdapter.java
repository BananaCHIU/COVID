package com.example.covid.ui.supplies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid.R;
import com.example.covid.data.Store;

import java.util.List;

public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static class StoreViewHolder extends RecyclerView.ViewHolder
    {
        public final CardView storeCardView;
        public final ImageView storeImage;
        public final TextView storeName;
        public final TextView suppliesType;

        public StoreViewHolder(View view)
        {
            super(view);
            storeCardView = (CardView)itemView.findViewById(R.id.store_CardView);
            storeImage = (ImageView)itemView.findViewById(R.id.store_Image);
            storeName = (TextView) itemView.findViewById(R.id.store_Name);
            suppliesType = (TextView) itemView.findViewById(R.id.store_SuppliesType);
        }
    }

    private List<Store> stores;

    public StoreRecyclerViewAdapter(List<Store> stores)
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

        String suppliesText = "";
        for(String type : stores.get(i).getSupplies())
        {
            suppliesText += type + "\n";
        }
        storeViewHolder.suppliesType.setText(suppliesText);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
