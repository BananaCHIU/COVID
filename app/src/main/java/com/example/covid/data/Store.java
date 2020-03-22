package com.example.covid.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Store
{
    private String storeID;
    private Bitmap image;
    private String name;
    private List<Supply> supplies;

    public Store() { }

    public Store(String id, Bitmap image, String name, List<Supply> supplies)
    {
        this.storeID = id;
        this.image = image;
        this.name = name;
        this.supplies = supplies;
    }

    public String getStoreID() { return storeID; }

    public Bitmap getImage() { return image; }

    public String getStoreName() { return name; }

    public List<Supply> getSupplies() { return supplies; }

    public void setImage(Bitmap image) { this.image = image; }

    public void setStoreName(String name) { this.name = name; }

    public void setSupplies(List<Supply> supplies) { this.supplies = supplies; }

    /*
    public static List<Store> sample_data()
    {
        ArrayList<Store> sample = new ArrayList<Store>(6);

        Store one = new Store(null, "Store 1", new ArrayList<Supply>());
        one.supplies.addAll(Arrays.asList(new Supply(Supply.suppliesType.SURGICAL_MASK, new ArrayList<Map<String, Double>>())));
        sample.add(one);

        Store two = new Store(null, "Store 2", new ArrayList<Supply>());
        two.supplies.addAll(Arrays.asList(new Supply(Supply.suppliesType.SURGICAL_MASK, new ArrayList<Map<String, Double>>()), new Supply(Supply.suppliesType.HAND_SANITIZER, new ArrayList<Map<String, Double>>())));
        sample.add(two);

        Store three = new Store(null, "Store 3", new ArrayList<Supply>());
        three.supplies.addAll(Arrays.asList(new Supply(Supply.suppliesType.SURGICAL_MASK, new ArrayList<Map<String, Double>>()), new Supply(Supply.suppliesType.HAND_SANITIZER, new ArrayList<Map<String, Double>>()), new Supply(Supply.suppliesType.BLEACH, new ArrayList<Map<String, Double>>())));
        sample.add(three);

        return sample;
    }
     */
}