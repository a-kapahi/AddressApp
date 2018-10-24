package com.example.addressapp.api;

public class APIUtils {

    public static final String API_URL = "http://shop-spree.herokuapp.com/api/ams/user/";

    private APIUtils() {
    }

    public static AddressService getUserService() {
        return RetrofitClient.getClient(API_URL).create(AddressService.class);
    }

}