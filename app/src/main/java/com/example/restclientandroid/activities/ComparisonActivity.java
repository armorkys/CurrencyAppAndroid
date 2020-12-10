package com.example.restclientandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restclientandroid.APICall;
import com.example.restclientandroid.R;
import com.example.restclientandroid.model.ConnectionModel;
import com.example.restclientandroid.model.FxRateHandling;
import com.example.restclientandroid.model.FxRatesHandling;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComparisonActivity extends AppCompatActivity {

    private List<String> currencyNamesList;
    private Map<String, Double> currencyList;
    private APICall myApiCall;
    private ArrayAdapter<String> adapter;
    private Spinner currencySpinner1, currencySpinner2;
    private EditText inputCurrency1;
    private TextView txtCurrency1, txtCurrency2;
    private double currencyResult1, currencyResult2, multiplier1, multiplier2;
    private String currencyName1, currencyName2;
    private static DecimalFormat df = new DecimalFormat("0.00");

    public void getCurrencyNamesList() {
        Call<List<String>> call = myApiCall.getCurrencyList();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.code() != 200) {
                    Toast.makeText(ComparisonActivity.this, "Check the connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                currencyNamesList.clear();
                currencyNamesList.add("EUR");
                for (String c : response.body()) {
                    currencyNamesList.add(c);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadCurrencyValues() {


        Call<FxRatesHandling> call = myApiCall.getFxRates();
        call.enqueue(new Callback<FxRatesHandling>() {
            @Override
            public void onResponse(Call<FxRatesHandling> call, Response<FxRatesHandling> response) {
                if (response.code() != 200) {
                    Toast.makeText(ComparisonActivity.this, "Check the connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                currencyList.put("EUR", 1.00d);
                for (FxRateHandling c : response.body().getFxRate()) {
                    currencyList.put(c.getCcyAmt().get(1).getCcy().toString(), c.getCcyAmt().get(1).getAmt().doubleValue());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<FxRatesHandling> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void convertCurrency() {
        if (inputCurrency1.getText().toString().equals("")) {
            return;
        }

        currencyName1 = currencySpinner1.getSelectedItem().toString();
        currencyName2 = currencySpinner2.getSelectedItem().toString();

        multiplier1 = currencyList.get(currencyName1);
        multiplier2 = currencyList.get(currencyName2);

        currencyResult1 = Double.parseDouble(inputCurrency1.getText().toString()) / multiplier1;
        currencyResult2 =  currencyResult1 * multiplier2;

       int maxSize = 99999999;

        if(currencyResult1 > maxSize || currencyResult2 > maxSize){
            Toast.makeText(this, "Please try smaller numbers or different currency", Toast.LENGTH_SHORT).show();
            return;
        } else if(currencyResult1 < 0 || currencyResult2 < 0){
            Toast.makeText(this, "Bad number inputs, result less than 0", Toast.LENGTH_SHORT).show();
            return;
        }else if(currencyResult1 < maxSize && currencyResult2 < maxSize) {
            txtCurrency1.setText(df.format(currencyResult1));
            txtCurrency2.setText(df.format(currencyResult2));
        }
    }

    public void openHistoryInputLayout(){
        Intent intent = new Intent(this, HistoricalInputActivity.class);
        startActivity(intent);
    }

    public void openHomeLayout(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        myApiCall = ConnectionModel.getConnection();

        currencyNamesList = new ArrayList<>();
        currencyList = new HashMap<>();

        Button btnHistorical = findViewById(R.id.btnHistorical);
        btnHistorical.setOnClickListener(v -> openHistoryInputLayout());

        Button btnAll = findViewById(R.id.btnAll);
        btnAll.setOnClickListener(v -> openHomeLayout());

        getCurrencyNamesList();
        loadCurrencyValues();

        currencySpinner1 = findViewById(R.id.spinnerCurrencyList1);
        currencySpinner2 = findViewById(R.id.spinnerCurrencyList2);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyNamesList);

        currencySpinner1.setAdapter(adapter);
        currencySpinner2.setAdapter(adapter);

        inputCurrency1 = findViewById(R.id.txtInputCurrencyAmount1);

        txtCurrency1 = findViewById(R.id.txtCurrency1);
        txtCurrency2 = findViewById(R.id.txtCurrency2);

        currencySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertCurrency();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        currencySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertCurrency();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inputCurrency1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convertCurrency();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
