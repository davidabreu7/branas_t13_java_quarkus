package com.branas.domain.enums;

public enum RideStateEnum {
    REQUESTED("REQUESTED"),
    ACCEPTED("ACCEPTED"),
    STARTED("STARTED"),
    FINISHED("FINISHED"),
    CANCELLED("CANCELLED");

    private final String value;

    RideStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
