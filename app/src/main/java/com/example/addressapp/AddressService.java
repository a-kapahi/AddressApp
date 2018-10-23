package com.example.addressapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AddressService {

    @GET("addresses")
    Call<List<Address>> getAddresses(@Query("token") String token);

    @POST("addresses")
    Call<Address> addAddress(@Query("token") String token, @Body Address address);

    @PUT("addresses/{id}")
    Call<Address> updateAddress(@Path("id") int id, @Query("token") String token, @Body Address address);

    @DELETE("addresses/{id}")
    Call<Address> deleteAddress(@Path("id") int id, @Query("token") String token);
}