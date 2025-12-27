package com.example.ec_sample.domain.order;

public enum Status {
    CREATED("注文作成"),
    PAID("支払い済"),
    SHIPPED("発送済み"),
    CANCELLED("キャンセル");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
