package com.example.restclientandroid.model;

import com.example.restclientandroid.APICall;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionModel {

    private static APICall myApiCall = null;


    private ConnectionModel(){}


    public static void createConnection(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://78.61.102.30:8089")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApiCall = retrofit.create(APICall.class);
    }

    public static APICall getConnection(){
        if(myApiCall == null){
            createConnection();
        }
            return myApiCall;
    }
}
