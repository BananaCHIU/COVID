package com.example.covid.data;

import java.util.List;
import java.util.Map;

public class Supply
{
    public enum suppliesType {SURGICAL_MASK, HAND_SANITIZER, BLEACH, RUBBING_ALCOHOL, RESPIRATOR, ISOLATION_CLOTHING}
    private suppliesType type;
    private List<Map<String, Double>> entity;

    public Supply() { }

    public Supply(suppliesType type, List<Map<String, Double>> entity)
    {
        this.type = type;
        this.entity = entity;
    }

    public suppliesType getType() { return type; }

    public List<Map<String, Double>> getEntity() { return entity; }

    public void setType(suppliesType type) { this.type = type; }

    public void setEntity(List<Map<String, Double>> entity) { this.entity = entity; }
}
