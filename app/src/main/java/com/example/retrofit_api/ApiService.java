package com.example.retrofit_api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.0.102/MyData/Service_API_Android/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("service.php")
    Call<ArrayList<Student>> getListUsers(@Query("state") int state);

    @POST("service.php")
    Call<Void> postUser(@Body postStudent student);
}
