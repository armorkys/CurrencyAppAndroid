package com.example.restclientandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restclientandroid.APICall;
import com.example.restclientandroid.R;
import com.example.restclientandroid.model.ConnectionModel;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoricalInputActivity extends AppCompatActivity {

    private EditText dateFrom, dateTo;
    private Button btnAll, btnSearch;
    private Spinner currencySpinner;
    private List<String> currencyList;
    private APICall myApiCall;
    private ArrayAdapter<String> adapter;

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openComparisonLayout() {
        Intent intent = new Intent(this, ComparisonActivity.class);
        startActivity(intent);
    }

    public void openHistoricResult(View v, String curName, String dateFrom, String dateTo) {
        Intent intent = new Intent(this, HistoricResultsActivity.class);
        intent.putExtra("CurrencyName",  curName);
        intent.putExtra("DateFrom",  dateFrom);
        intent.putExtra("DateTo",  dateTo);
        startActivity(intent);
    }

    public void getCurrencyNamesList() {
        Call<List<String>> call = myApiCall.getCurrencyList();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.code() != 200) {
                    Toast.makeText(HistoricalInputActivity.this, "Check the connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                currencyList.clear();
                for (String c : response.body()) {
                    currencyList.add(c);
                }
            adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public boolean validDate(String date) {
        if (date.length() != 10) {
            Toast.makeText(this, "Must be in \"yyy-mm-dd\" format", Toast.LENGTH_SHORT).show();
            return false;
        }
        Integer year, month, day;
        try {
            year = Integer.valueOf(date.substring(0, 4));
            month = Integer.valueOf(date.substring(5, 7));
            day = Integer.valueOf(date.substring(8, 10));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Bad number format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (year <= 2014 && month <= 9 && day < 30) {
            Toast.makeText(this, "Date must be later than 2014-09-29", Toast.LENGTH_LONG).show();
            return false;
        }

        if (year == null || month == null || day == null) {
            Toast.makeText(this, "Must be in \"yyy-mm-dd\" format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (year < 2014 || year > 2020 || month < 1 || month > 12 || day < 1 || day > 31) {
            Toast.makeText(this, "Dates out of range", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date.charAt(4) != '-' && date.charAt(7) != '-') {
            Toast.makeText(this, "Bad symbol used as date seperator", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_input);

        btnAll = findViewById(R.id.btnAll);
        btnAll.setOnClickListener(v -> openHomeActivity());

        Button btnComparison = findViewById(R.id.btnComparison);
        btnComparison.setOnClickListener(v -> openComparisonLayout());

        btnSearch = findViewById(R.id.btnHistorySearch);
        btnSearch.setOnClickListener(v -> {
            if(validDate(dateFrom.getText().toString()) && validDate(dateTo.getText().toString())){

                String dateA = dateFrom.getText().toString();
                String dateB = dateTo.getText().toString();
               int year1 = Integer.parseInt(dateA.substring(0, 4));
                int month1 = Integer.parseInt(dateA.substring(5, 7));
                int day1 = Integer.parseInt(dateA.substring(8, 10));
                GregorianCalendar gCalendar1 = new GregorianCalendar(year1, month1-1, day1);

               int year2 = Integer.parseInt(dateB.substring(0, 4));
                int month2 = Integer.parseInt(dateB.substring(5, 7));
                int day2 = Integer.parseInt(dateB.substring(8, 10));
                GregorianCalendar gCalendar2 = new GregorianCalendar(year2, month2-1, day2);

                if(gCalendar1.after(gCalendar2)){
                    Toast.makeText(this, "\"Date From\" higher value than \"Date To\"", Toast.LENGTH_SHORT).show();
                    return;
                }

                String curName = currencySpinner.getSelectedItem().toString();
                openHistoricResult(v, curName, dateFrom.getText().toString(), dateTo.getText().toString());
            }
        });

        myApiCall = ConnectionModel.getConnection();

        currencyList = new ArrayList<>();

        dateFrom = findViewById(R.id.txtInputDateFrom);
        dateTo = findViewById(R.id.txtInputDateTo);

        getCurrencyNamesList();

        currencySpinner = findViewById(R.id.spinnerCurrencyList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencyList);
        currencySpinner.setAdapter(adapter);
    }
}
