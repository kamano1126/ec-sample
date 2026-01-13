package com.example.ec_sample.domain.product;

public enum Category {
    JACKET("jakect"),
    PANTS("pants"),
    SHIRT("shirt"),
    TSHIRT("T-shirt"),
    LIGHTOUTER("light-outer");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
