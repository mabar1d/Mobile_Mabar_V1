package com.example.mabar_v1.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private static String BASE_URL = "https://apimabar.vidiwijaya.my.id/api/";

    public static ApiService getApiServices(String token){

        /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.level(HttpLoggingInterceptor.Level.BODY);*/
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", " Bearer " + token )
                        .build();
                return chain.proceed(newRequest);
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        return  service;
    }
}
