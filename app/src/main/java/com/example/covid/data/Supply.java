package com.example.covid.data;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Supply implements Serializable
{
    public enum suppliesType {SURGICAL_MASK, HAND_SANITIZER, BLEACH, RUBBING_ALCOHOL, RESPIRATOR, ISOLATION_CLOTHING}
    private suppliesType type;
    private String name;
    private double price;

    public Supply() { }

    public Supply(suppliesType type, String name, double price)
    {
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public suppliesType getType() { return type; }

    public double getPrice() { return price; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public void setPrice(double price) { this.price = price; }

    public void setType(suppliesType type) { this.type = type; }

}
