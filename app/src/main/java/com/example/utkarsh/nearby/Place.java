package com.example.utkarsh.nearby;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by utkarsh on 12/7/16.
 */
public class Place {
    private String Address;
    private Float probability;
    private String name;
    private LatLng x;


    public Place(String name, String Address, Float probability) {
        this.Address = Address;
        this.probability = probability;
        this.name = name;
     //   this.x = x;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public Float getprobability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public LatLng getlatlag() {
        return x;
    }

}
