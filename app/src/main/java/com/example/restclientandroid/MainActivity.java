package com.example.restclientandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restclientandroid.model.CustomInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ClipData.newIntent;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    List<CustomInfo> notes = new ArrayList<>();

    public void preLoadInfoMain() {
        notes = Arrays.asList(
                new CustomInfo("A1", 4, 2.5),
                new CustomInfo("B2", 6, 7.5),
                new CustomInfo("C3", 7, 9.5),
                new CustomInfo("D4", 9, 6.5),
                new CustomInfo("E5", 2, 7.5)
        );
    }


    public void loadHistoricalAView(View v) {
        setContentView(R.layout.activity_historical_input);

    }

    public void loadHistoricalBView(View v) {
        setContentView(R.layout.activity_historical_result);

    }

    public void loadComparisonView(View v) {
        setContentView(R.layout.activity_compare);

    }

    public void loadFavouritesView(View v) {
        setContentView(R.layout.activity_home);

        getRestMulti();

        ListView listview = findViewById(R.id.listFavouriteCurrencies);
        ArrayAdapter<CustomInfo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listview.setAdapter(adapter);

    }

    public void loadFullListView(View v) {
        setContentView(R.layout.activity_show_all);

        getRestMulti();

        ListView listview = findViewById(R.id.listFavouriteCurrencies);
        ArrayAdapter<CustomInfo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listview.setAdapter(adapter);

    }

    public void loadTestView(View v) {
        setContentView(R.layout.activity_test);

    }

    public void compareCurrencies(View v){

    }

    public void showListConsole(View v) {
        System.out.println("\n############");
        for (CustomInfo c : notes) {
            System.out.println(c.getData1() + " " + c.getData2() + " " + c.getData3());
        }
    }

    public void getRestSingle() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://78.61.102.30:8089/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APICall myApiCall = retrofit.create(APICall.class);

        Call<CustomInfo> call = myApiCall.getInfoSingle();
        call.enqueue(new Callback<CustomInfo>() {
            @Override
            public void onResponse(Call<CustomInfo> call, Response<CustomInfo> response) {
                if (response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Check the connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<CustomInfo> customNotes = new ArrayList<>();
                customNotes.add(new CustomInfo(
                        response.body().getData1(),
                        response.body().getData2(),
                        response.body().getData3()));
                notes.clear();
                notes = customNotes;
            }

            @Override
            public void onFailure(Call<CustomInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getRestMulti() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://78.61.102.30:8089/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APICall myApiCall = retrofit.create(APICall.class);

        Call<List<CustomInfo>> call = myApiCall.getInfoAll();

        call.enqueue(new Callback<List<CustomInfo>>() {
            @Override
            public void onResponse(Call<List<CustomInfo>> call, Response<List<CustomInfo>> response) {
                if (response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Check the connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<CustomInfo> customNotes = new ArrayList<>();

                for (CustomInfo c : response.body()) {
                    customNotes.add(new CustomInfo(
                            c.getData1(),
                            c.getData2(),
                            c.getData3()));
                }
                notes.addAll(customNotes);
                // notes = customNotes;
            }

            @Override
            public void onFailure(Call<List<CustomInfo>> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getRestMulti();

        ListView listview = findViewById(R.id.listFavouriteCurrencies);
        ArrayAdapter<CustomInfo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listview.setAdapter(adapter);
    }


}