//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.restclientandroid.model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


public class FxRateHandling {

    protected FxRateTypeHandling tp;

    protected String dt;

    protected List<CcyAmtHandling> ccyAmt;

    public FxRateHandling() {
    }

    public FxRateTypeHandling getTp() {
        return this.tp;
    }

    public void setTp(FxRateTypeHandling value) {
        this.tp = value;
    }

    public String getDt() {
        return this.dt;
    }

    public void setDt(String value) {
        this.dt = value;
    }

    public List<CcyAmtHandling> getCcyAmt() {
        if (this.ccyAmt == null) {
            this.ccyAmt = new ArrayList();
        }

        return this.ccyAmt;
    }

    @Override
    public String toString() {
        return   dt.toString().substring(0, 4) + "-" + dt.toString().substring(5,7) + "-" + dt.toString().substring(8, 10)
                + " " + ccyAmt.get(1).getCcy() + " " + ccyAmt.get(1).getAmt();
    }
}
