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

public interface UserService {

    @GET("addresses")
    Call<List<User>> getUsers(@Query("token") String token);

    @POST("addresses")
    Call<User> addUser(@Query("token") String token, @Body User user);

    @PUT("addresses/{id}")
    Call<User> updateUser(@Path("id") int id,@Query("token") String token, @Body User user);

    @DELETE("addresses/{id}")
    Call<User> deleteUser(@Path("id") int id, @Query("token") String token);
}