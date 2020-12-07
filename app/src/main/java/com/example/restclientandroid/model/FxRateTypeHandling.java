package com.example.restclientandroid.model;


public enum FxRateTypeHandling {
    LT,
    EU;

    private FxRateTypeHandling() {
    }

    public String value() {
        return this.name();
    }

    public static FxRateTypeHandling fromValue(String v) {
        return valueOf(v);
    }
}
