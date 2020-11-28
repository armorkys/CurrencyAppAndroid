package com.example.restclientandroid.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CCurency {


    //This class will be as a template for the data that we are going to parse

    @SerializedName("id")
    private Integer id;
    @SerializedName("ccy")
    private String ccy;
    @SerializedName("amt")
    private BigDecimal amt;
    @SerializedName("dt")
    private LocalDate dt;
    @SerializedName("tp")
    private String tp;


    public CCurency(String ccy, BigDecimal amt, LocalDate dt, String tp) {
        this.ccy = ccy;
        this.amt = amt;
        this.dt = dt;
        this.tp = tp;
    }

    public CCurency(int id, String ccy, BigDecimal amt, LocalDate dt, String tp) {
        this.id = id;
        this.ccy = ccy;
        this.amt = amt;
        this.dt = dt;
        this.tp = tp;
    }

    public CCurency() {}

    public Integer getId() {
        return id;
    }

    public String getCcy() {
        return ccy;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public LocalDate getDt() {
        return dt;
    }

    public String getTp() {
        return tp;
    }

    @Override
    public String toString() {
        return "CurrencyRatesHistory{" +
                "id=" + id +
                ", ccy=" + ccy +
                ", ant=" + amt +
                ", dt=" + dt +
                ", tp=" + tp +
                '}';
    }

}
