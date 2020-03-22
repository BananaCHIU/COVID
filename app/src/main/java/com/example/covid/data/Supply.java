package com.example.covid.data;

import java.util.List;
import java.util.Map;

public class Supply
{
    public enum suppliesType {SURGICAL_MASK, HAND_SANITIZER, BLEACH, RUBBING_ALCOHOL, RESPIRATOR, ISOLATION_CLOTHING}
    private suppliesType type;
    private String name;
    private double price;
    //private List<Map<String, Double>> entity;

    public Supply() { }

    public Supply(suppliesType type, String name, double price)
    {
        this.type = type;
        this.name = name;
        this.price = price;
        //this.entity = entity;
    }

    public suppliesType getType() { return type; }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //public List<Map<String, Double>> getEntity() { return entity; }

    public void setType(suppliesType type) { this.type = type; }

    //public void setEntity(List<Map<String, Double>> entity) { this.entity = entity; }
}
