package com.example.covid.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Store
{
    private Bitmap image;
    private String name;
    private List<String> supplies;

    public Store() { }

    public Store(Bitmap image, String name, List<String> supplies)
    {
        this.image = image;
        this.name = name;
        this.supplies = supplies;
    }

    public Bitmap getImage() { return image; }

    public String getStoreName() { return name; }

    public List<String> getSupplies() { return supplies; }

    public void setImage(Bitmap image) { this.image = image; }

    public void setStoreName(String name) { this.name = name; }

    public void setSupplies(List<String> supplies) { this.supplies = supplies; }

    public static List<Store> sample_data()
    {
        ArrayList<Store> sample = new ArrayList<Store>();
        for(int i = 0; i < 10; i++)
        {
            ArrayList<String> sup = new ArrayList<String>();
            for(int k = 0; k <= i; k++)
            {
                sup.add(Integer.toString(k));
            }

            Store s = new Store(null, "Store " + i + " Name", sup);

            sample.add(s);
        }
        return sample;
    }
}
