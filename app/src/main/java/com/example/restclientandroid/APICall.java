package com.example.restclientandroid;

import com.example.restclientandroid.model.CCurency;
import com.example.restclientandroid.model.CustomInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public interface APICall {

    //"http://localhost:8089/android/"
    //"http://localhost:8089/android/1"
    //"http://localhost:8089/android/all"

    @GET("single")
    Call<CCurency> getCurrencySingle();

    @GET("all")
    Call<List<CCurency>> getAllCurrency();

    @GET("infoSingle")
    Call<CustomInfo> getInfoSingle();

    @GET("infoAll")
    Call<List<CustomInfo>> getInfoAll();

    @GET("infoSingle")
     public void getCustomInfoSingle(Callback<CustomInfo> callback);

}
