package com.example.ec_sample.domain.product;

public enum Brand {
    LEVIS("LEVI'S"),
    USARMY("U.S.ARMY"),
    LLBEAN("L.L.BEAN"),
    CARHART("carhart"),
    MONTBELL("mont-BELL"),
    NOBRAND("ノーブランド"),
    FFA("FFA"),
    POLO("POLO COUNTRY");

    private final String label;

    Brand(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
