//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.restclientandroid.model;

import java.util.ArrayList;
import java.util.List;


public class FxRatesHandling {

    protected List<FxRateHandling> fxRate;

    protected OprlErrHandling oprlErr;

    public FxRatesHandling() {
    }

    public List<FxRateHandling> getFxRate() {
        if (this.fxRate == null) {
            this.fxRate = new ArrayList();
        }

        return this.fxRate;
    }

    public OprlErrHandling getOprlErr() {
        return this.oprlErr;
    }

    public void setOprlErr(OprlErrHandling value) {
        this.oprlErr = value;
    }


}
