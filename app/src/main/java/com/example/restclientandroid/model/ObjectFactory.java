
package com.example.restclientandroid.model;

import javax.xml.namespace.QName;


public class ObjectFactory {
    public ObjectFactory() {
    }

    public FxRatesHandling createFxRatesHandling() {
        return new FxRatesHandling();
    }

    public FxRateHandling createFxRateHandling() {
        return new FxRateHandling();
    }

    public CcyAmtHandling createCcyAmtHandling() {
        return new CcyAmtHandling();
    }

    public OprlErrHandling createOprlErrHandling() {
        return new OprlErrHandling();
    }

    public ErrorCode createErrorCode() {
        return new ErrorCode();
    }

}
