package com.example.addressapp;

public class APIUtils {

    public static final String API_URL = "http://shop-spree.herokuapp.com/api/ams/user/";

    private APIUtils() {
    }

    public static UserService getUserService() {
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

}