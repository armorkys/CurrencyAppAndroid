package com.example.restclientandroid;

import com.example.restclientandroid.model.FxRateHandling;
import com.example.restclientandroid.model.FxRatesHandling;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APICall {

    @GET("/getCurrent")
    Call<FxRatesHandling> getFxRates();


    @GET("/getCurrencyHistory/{ccy}")
    Call<FxRatesHandling> getCurrencyHistory(
            @Path("ccy") String ccy);

    @GET("/getCurrencyHistoryCustom/{ccy}+from={dateFrom}+dateTo{dateTo}")
    Call<FxRatesHandling> getCurrencyHistoryCustom(
            @Path("ccy") String ccy,
            @Path("dateFrom") String dateFrom,
            @Path("dateTo") String dateTo
    );


    @GET("/getCurrencyList")
    Call<List<String>> getCurrencyList();


//    @GET("all")
//    Call<List<CCurency>> getAllCurrency();
//
//    @GET("infoSingle")
//    Call<CustomInfo> getInfoSingle();
//
//    @GET("infoAll")
//    Call<List<CustomInfo>> getInfoAll();
//
//    @GET("infoSingle")
//     public void getCustomInfoSingle(Callback<CustomInfo> callback);

}
