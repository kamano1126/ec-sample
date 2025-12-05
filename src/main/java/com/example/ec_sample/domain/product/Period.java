package com.example.ec_sample.domain.product;

import java.awt.*;

public enum Period {
    VINTAGE_40s("1940年代"),
    VINTAGE_50s("1950年代"),
    VINTAGE_60s("1960年代"),
    VINTAGE_70s("1970年代"),
    VINTAGE_80s("1980年代"),
    VINTAGE_90s("1990年代"),
    REGULAR("現行");

    private final String label;

    Period(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
