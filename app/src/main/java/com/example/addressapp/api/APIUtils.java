package com.example.addressapp.api;

public class APIUtils {

    private static final String API_URL = "http://shop-spree.herokuapp.com/api/ams/user/";

    private APIUtils() {
    }

    public static AddressService getAddressService() {
        return RetrofitClient.getClient(API_URL).create(AddressService.class);
    }

}