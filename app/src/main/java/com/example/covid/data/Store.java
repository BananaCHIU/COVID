package com.example.covid.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Store
{
    public enum suppliesType {SURGICAL_MASK, HAND_SANITIZER, BLEACH, RUBBING_ALCOHOL, RESPIRATOR, ISOLATION_CLOTHING}
    private Bitmap image;
    private String name;
    private List<suppliesType> supplies;

    public Store() { }

    public Store(Bitmap image, String name, List<suppliesType> supplies)
    {
        this.image = image;
        this.name = name;
        this.supplies = supplies;
    }

    public Bitmap getImage() { return image; }

    public String getStoreName() { return name; }

    public List<suppliesType> getSupplies() { return supplies; }

    public void setImage(Bitmap image) { this.image = image; }

    public void setStoreName(String name) { this.name = name; }

    public void setSupplies(List<suppliesType> supplies) { this.supplies = supplies; }

    public static List<Store> sample_data()
    {
        ArrayList<Store> sample = new ArrayList<Store>(6);

        Store one = new Store(null, "Store 1", new ArrayList<suppliesType>());
        one.supplies.addAll(Arrays.asList(suppliesType.SURGICAL_MASK));
        sample.add(one);

        Store two = new Store(null, "Store 2", new ArrayList<suppliesType>());
        two.supplies.addAll(Arrays.asList(suppliesType.SURGICAL_MASK, suppliesType.HAND_SANITIZER));
        sample.add(two);

        Store three = new Store(null, "Store 3", new ArrayList<suppliesType>());
        three.supplies.addAll(Arrays.asList(suppliesType.SURGICAL_MASK, suppliesType.HAND_SANITIZER, suppliesType.BLEACH));
        sample.add(three);

        Store four = new Store(null, "Store 4", new ArrayList<suppliesType>());
        four.supplies.addAll(Arrays.asList(suppliesType.SURGICAL_MASK, suppliesType.HAND_SANITIZER, suppliesType.BLEACH, suppliesType.RUBBING_ALCOHOL));
        sample.add(four);

        Store five = new Store(null, "Store 5", new ArrayList<suppliesType>());
        five.supplies.addAll(Arrays.asList(suppliesType.SURGICAL_MASK, suppliesType.HAND_SANITIZER, suppliesType.BLEACH, suppliesType.RUBBING_ALCOHOL, suppliesType.RESPIRATOR));
        sample.add(five);

        Store six = new Store(null, "Store 6", new ArrayList<suppliesType>());
        six.supplies.addAll(Arrays.asList(suppliesType.SURGICAL_MASK, suppliesType.HAND_SANITIZER, suppliesType.BLEACH, suppliesType.RUBBING_ALCOHOL, suppliesType.RESPIRATOR, suppliesType.ISOLATION_CLOTHING));
        sample.add(six);

        return sample;
    }
}

/*
for(int i = 0; i < 6; i++)
        {
            ArrayList<suppliesType> sup = new ArrayList<suppliesType>();
            for(int k = 0; k <= i; k++)
            {
                sup.add(Integer.toString(k));
            }

            Store s = new Store(null, "Store " + i + " Name", sup);

            sample.add(s);
        }
 */