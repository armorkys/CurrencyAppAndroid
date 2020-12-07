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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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


    public void historicDataReverse() {
        List<FxRateHandling> historicDataReversed = new ArrayList<>();
        for (int i = historicData.size() - 1; i > 0; i--) {
            historicDataReversed.add(historicData.get(i));
        }
        historicData = historicDataReversed;
    }

    public void graphLoad() {
        historicDataReverse();

        List<Float> values = new ArrayList<>();

        List<Date> datesList = new ArrayList<>();
        GregorianCalendar gCalendar;
        int year, month, day;

        for (FxRateHandling f : historicData) {
            year = Integer.parseInt(f.getDt().substring(0, 4));
            month = Integer.parseInt(f.getDt().substring(5, 7));
            day = Integer.parseInt(f.getDt().substring(8, 10));
            gCalendar = new GregorianCalendar(year, month, day);
            datesList.add(gCalendar.getTime());
        }

        for (FxRateHandling f : historicData) {
            values.add((f.getCcyAmt().get(1).getAmt()).floatValue());
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
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(HistoricResultsActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setNumVerticalLabels(8);


        Float maxValue = values.stream().max(Comparator.comparing(Float::floatValue)).get();
        Float minValue = values.stream().min(Comparator.comparing(Float::floatValue)).get();

        graph.getViewport().setMinY(minValue);
        graph.getViewport().setMaxY(maxValue);
        graph.getViewport().setYAxisBoundsManual(true);


        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getViewport().setScalableY(true);
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
            dateFrom = (currentDate.get(Calendar.YEAR) + -1) + "-" + (currentDate.get(Calendar.MONTH)+1)+ "-" + currentDate.get(Calendar.DAY_OF_MONTH);
            dateTo = currentDate.get(Calendar.YEAR) + "-" + (currentDate.get(Calendar.MONTH)+1)+ "-" + currentDate.get(Calendar.DAY_OF_MONTH);
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