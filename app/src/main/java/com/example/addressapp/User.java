package com.example.addressapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("token")
    @Expose
    public static final String token= "52e04d83e87e509f07982e6ac851e2d2c67d1d0eabc4fe78";

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("firstname")
    @Expose
    private String name;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("country")
    @Expose
    private int country;

    @SerializedName("state")
    @Expose
    private int state;

    @SerializedName("zip")
    @Expose
    private int zip;

    @SerializedName("phone")
    @Expose
    private long phone;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.city = "SanFran";
        this.country = 105;
        this.state = 1400;
        this.zip = 111111;
        this.phone = 1111111111;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}