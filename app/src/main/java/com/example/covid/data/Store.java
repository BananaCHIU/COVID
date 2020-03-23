package com.example.covid.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class Store implements Parcelable
{
    private String storeID;
    private Bitmap image;
    private String name;
    private Map<String, ArrayList> supplies;
    private String district, address, timeOpen, timeClose;
    private boolean approved;


    public Store() { }

    public Store(String id, Bitmap image, String name, Map<String, ArrayList> supplies, String district, String address, String timeOpen, String timeClose, boolean approved)
    {
        this.storeID = id;
        this.image = image;
        this.name = name;
        this.supplies = supplies;
        this.district = district;
        this.address = address;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.approved = approved;
    }

    protected Store(Parcel in) {
        storeID = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        name = in.readString();
        district = in.readString();
        address = in.readString();
        timeOpen = in.readString();
        timeClose = in.readString();
        approved = in.readByte() != 0;
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public String getStoreID() { return storeID; }

    public Bitmap getImage() { return image; }

    public String getStoreName() { return name; }

    public Map<String, ArrayList> getSupplies() { return supplies; }

    public String getDistrict() { return district; }

    public String getAddress() { return address; }

    public String getTimeOpen() { return timeOpen; }

    public String getTimeClose() { return timeClose; }

    public boolean isApproved() { return approved; }

    public void setStoreID(String id) { this.storeID = id; }

    public void setImage(Bitmap image) { this.image = image; }

    public void setStoreName(String name) { this.name = name; }

    public void setSupplies(Map<String, ArrayList> supplies) { this.supplies = supplies; }

    public void setDistrict(String district) { this.district = district; }

    public void setAddress(String address) { this.address = address; }

    public void setTimeOpen(String timeOpen) { this.timeOpen = timeOpen; }

    public void setTimeClose(String timeClose) { this.timeClose = timeClose; }

    public static ArrayList<Store> sample_data()
    {
        Map<String, ArrayList> supplyType = new HashMap<>();
        ArrayList<Supply> s = new ArrayList<>();
        s.add(new Supply(Supply.suppliesType.SURGICAL_MASK, "test 1", 30.0));
        s.add(new Supply(Supply.suppliesType.SURGICAL_MASK, "test 2", 580.0));
        supplyType.put(Supply.suppliesType.SURGICAL_MASK.toString(), s);

        ArrayList<Store> sample = new ArrayList<>();
        sample.add(new Store("123", null, "Store 1", supplyType, "Wong Tai Sin", "ksjdhfksjdfhksjdhf", "10:00", "20:00", true));

        return sample;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(storeID);
        parcel.writeParcelable(image, i);
        parcel.writeString(name);
        parcel.writeString(district);
        parcel.writeString(address);
        parcel.writeString(timeOpen);
        parcel.writeString(timeClose);
        parcel.writeByte((byte) (approved ? 1 : 0));
    }
}