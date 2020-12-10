package com.example.restclientandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restclientandroid.APICall;
import com.example.restclientandroid.R;
import com.example.restclientandroid.model.ConnectionModel;
import com.example.restclientandroid.model.FxRateHandling;
import com.example.restclientandroid.model.FxRatesHandling;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricResultsActivity extends AppCompatActivity {

    private String currencyName;
    private String dateFrom, dateTo;
    private GraphView graph;
    private List<FxRateHandling> historicData;
    private APICall myApiCall;
    private Calendar currentDate;
    private  HashMap<String, Float> hMap;
    private TreeMap<String, Float> sortedMap;

    public static TreeMap<String, Float> sortByKey(HashMap<String, Float> mapToSort) {
        TreeMap<String, Float> sortedMap = new TreeMap<>();
        sortedMap.putAll(mapToSort);
        return sortedMap;
    }

    public void graphLoad() {

       hMap = new HashMap<>();

        int year, month, day;

        for (FxRateHandling f : historicData) {
            hMap.put(f.getDt(), (f.getCcyAmt().get(1).getAmt()).floatValue());
        }

        sortedMap = sortByKey(hMap);
        List<Float> values = new ArrayList<>();

        List<Date> datesList = new ArrayList<>();
        GregorianCalendar gCalendar, gCalendarTemp = null;

        for (Map.Entry<String, Float> entry : sortedMap.entrySet()) {
            String s = entry.getKey();
            Float aDouble = entry.getValue();
            year = Integer.parseInt(s.substring(0, 4));
            month = Integer.parseInt(s.substring(5, 7));
            day = Integer.parseInt(s.substring(8, 10));
            gCalendar = new GregorianCalendar(year, month-1, day);

            if(gCalendarTemp == null){
                gCalendarTemp = gCalendar;
                System.out.println("First iteration");
                datesList.add(gCalendar.getTime());
                values.add(aDouble);
            } else{
                if(gCalendar.after(gCalendarTemp) ==false){
                    System.out.println("Skipped unsorted value");
                } else{
                    datesList.add(gCalendar.getTime());
                    values.add(aDouble);
                    gCalendarTemp = gCalendar;
                }
            }
        }

        DataPoint[] dateArray = new DataPoint[values.size()];

        try {
            for (int i = 0; i < values.size(); i++) {
                dateArray[i] = new DataPoint(datesList.get(i), values.get(i));
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(HistoricResultsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dateArray);

        graph.addSeries(series);

        Float maxValue = values.stream().max(Comparator.comparing(Float::floatValue)).get();
        Float minValue = values.stream().min(Comparator.comparing(Float::floatValue)).get();

        graph.getViewport().setMinY(minValue);
        graph.getViewport().setMaxY(maxValue);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getViewport().setScalable(true);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(HistoricResultsActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        graph.getGridLabelRenderer().setNumVerticalLabels(8);

        //graph.setCursorMode(true);
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentDate = Calendar.getInstance();
        setContentView(R.layout.activity_historical_result);
        myApiCall = ConnectionModel.getConnection();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> openHomeActivity());

        historicData = new ArrayList<>();
        Intent intent = getIntent();
        currencyName = intent.getExtras().getString("CurrencyName");

        dateFrom = intent.getExtras().getString("DateFrom");
        dateTo = intent.getExtras().getString("DateTo");

        Call<FxRatesHandling> call;

        if (dateFrom == null || dateTo == null) {
            call = myApiCall.getCurrencyHistory(currencyName);
            dateFrom = (currentDate.get(Calendar.YEAR) + -1) + "-" + (currentDate.get(Calendar.MONTH) + 1) + "-" + currentDate.get(Calendar.DAY_OF_MONTH);
            dateTo = currentDate.get(Calendar.YEAR) + "-" + (currentDate.get(Calendar.MONTH) + 1) + "-" + currentDate.get(Calendar.DAY_OF_MONTH);
        } else {
            call = myApiCall.getCurrencyHistoryCustom(currencyName, dateFrom, dateTo);
        }

        TextView currencyTextView = findViewById(R.id.txtCurrencyName);
        TextView currencyDatesTextView = findViewById(R.id.txtCurrencyDates);

        currencyTextView.setText(currencyName + " to 1 Eur | Ratio History");
        currencyDatesTextView.setText(dateFrom + " - " + dateTo);

        call.enqueue(new Callback<FxRatesHandling>() {
            @Override
            public void onResponse(Call<FxRatesHandling> call, Response<FxRatesHandling> response) {
                if (response.code() != 200) {
                    Toast.makeText(HistoricResultsActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    System.out.println("Error " + response.code());
                    return;
                }
                historicData.clear();
                for (FxRateHandling f : response.body().getFxRate()) {
                    historicData.add(f);
                }
                graphLoad();
            }

            @Override
            public void onFailure(Call<FxRatesHandling> call, Throwable t) {
                t.printStackTrace();
            }
        });

        graph = findViewById(R.id.graph);
        graph.setVisibility(View.VISIBLE);

    }
}