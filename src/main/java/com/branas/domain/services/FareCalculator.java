package com.branas.domain.services;

public class FareCalculator {

    private FareCalculator() {
    }
    private static final Double MINIMUM_FARE = 3.0;
    private static final Double PRICE_PER_KM = 1.5;
    private static final Double MINIMUM_DISTANCE = 1.0;

    public static Double calculate(Double distance) {
        if(distance < MINIMUM_DISTANCE) {
            distance = MINIMUM_DISTANCE;
        }
        Double fare = MINIMUM_FARE + (distance * PRICE_PER_KM);
        return Math.round(fare * 100.0) / 100.0;
    }
}
