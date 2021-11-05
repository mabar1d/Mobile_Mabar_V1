package com.example.mabar_v1.retrofit;

import com.example.mabar_v1.login.model.ResponseLoginModel;
import com.example.mabar_v1.signup.model.ResponseRegisterModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("register")
    Call<ResponseRegisterModel> register(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password,
            @Query("password_confirmation") String password_confirmation
    );

    @POST("login")
    Call<ResponseLoginModel> login(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );

    /*@POST("refresh")
    Call<> refreshToken(
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password
    );*/


}
