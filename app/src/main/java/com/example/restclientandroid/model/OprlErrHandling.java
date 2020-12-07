package com.example.restclientandroid.model;

public class OprlErrHandling {

    protected ErrorCode err;

    protected String desc;

    public OprlErrHandling() {
    }

    public ErrorCode getErr() {
        return this.err;
    }

    public void setErr(ErrorCode value) {
        this.err = value;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String value) {
        this.desc = value;
    }
}
