package com.example.restclientandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restclientandroid.APICall;
import com.example.restclientandroid.R;
import com.example.restclientandroid.model.ConnectionModel;
import com.example.restclientandroid.model.FxRateHandling;
import com.example.restclientandroid.model.FxRatesHandling;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private List<FxRateHandling> currencyList;
    private APICall myApiCall;

    public void openHistoryResultLayout(String ccy) {
        Intent intent = new Intent(this, HistoricResultsActivity.class);
         intent.putExtra("CurrencyName",  ccy);
        startActivity(intent);
    }

    public void openHistoryInputLayout() {
        Intent intent = new Intent(this, HistoricalInputActivity.class);
        startActivity(intent);
    }

    private void openComparisonLayout() {
        Intent intent = new Intent(this, ComparisonActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currencyList = new ArrayList<>();

        Button btnHistorical = findViewById(R.id.btnHistorical);
        btnHistorical.setOnClickListener(v -> openHistoryInputLayout());

        Button btnComparison = findViewById(R.id.btnComparison);
        btnComparison.setOnClickListener(v -> openComparisonLayout());

        ListView currencyListView = findViewById(R.id.listViewCurrencies);
        currencyListView.setOnItemLongClickListener((parent, view, position, id) -> {
            openHistoryResultLayout(currencyList.get(position).getCcyAmt().get(1).getCcy().toString());
            return false;
        });

        ArrayAdapter<FxRateHandling> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyList);
        currencyListView.setAdapter(adapter);

        myApiCall = ConnectionModel.getConnection();
        Call<FxRatesHandling> call = myApiCall.getFxRates();

        call.enqueue(new Callback<FxRatesHandling>() {
            @Override
            public void onResponse(Call<FxRatesHandling> call, Response<FxRatesHandling> response) {
                if (response.code() != 200) {
                    Toast.makeText(HomeActivity.this, "Check the connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (FxRateHandling c : response.body().getFxRate()) {
                    currencyList.add(c);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<FxRatesHandling> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}


