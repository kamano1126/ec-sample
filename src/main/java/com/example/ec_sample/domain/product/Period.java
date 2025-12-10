package com.example.ec_sample.domain.product;

import java.awt.*;

public enum Period {
    VINTAGE_40S("1940年代"),
    VINTAGE_50S("1950年代"),
    VINTAGE_60S("1960年代"),
    VINTAGE_70S("1970年代"),
    VINTAGE_80S("1980年代"),
    VINTAGE_90S("1990年代"),
    REGULAR("現行");

    private final String label;

    Period(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
