package com.branas.domain.services;

import java.time.LocalDateTime;

public class FareCalculator {

    private static final Double MINIMUM_FARE = 3.0;
    private static final Double PRICE_PER_KM = 1.5;
    private static final Double PRICE_PER_MINUTE = 0.5;
    private static final Double MINIMUM_DISTANCE = 1.0;
    private static final Double MINIMUM_TIME = 5.0;

    public static Double calculate(Double distance, LocalDateTime time) {
        Double minutesRide = (double) (LocalDateTime.now().getMinute() - time.getMinute());
        if(distance < MINIMUM_DISTANCE) {
            distance = MINIMUM_DISTANCE;
        }
        if(minutesRide < MINIMUM_TIME) {
            minutesRide = MINIMUM_TIME;
        }
        return MINIMUM_FARE + (distance * PRICE_PER_KM) + (minutesRide * PRICE_PER_MINUTE);
    }
}
