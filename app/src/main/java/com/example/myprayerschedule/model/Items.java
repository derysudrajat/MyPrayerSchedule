package com.example.myprayerschedule.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {
    @SerializedName("items")
    public List<Jadwal> items;
    @SerializedName("state")
    public String state;
    @SerializedName("country")
    public String country;

    public Items(List<Jadwal> items) {
        this.items = items;
    }

    public List<Jadwal> getItems() {
        return items;
    }

    public void setItems(List<Jadwal> items) {
        this.items = items;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
