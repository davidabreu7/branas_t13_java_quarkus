package com.branas.domain.services;

import com.branas.domain.valueObjects.Coordinate;

public class DistanceCalculator {

    private DistanceCalculator() {
    }
    private static final int EARTH_RADIUS_KM = 6371;

    public static Double calculate(Coordinate fromCoordinate, Coordinate toCoordinate) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(fromCoordinate.getLatitude());
        double lon1Rad = Math.toRadians(fromCoordinate.getLongitude());
        double lat2Rad = Math.toRadians(toCoordinate.getLatitude());
        double lon2Rad = Math.toRadians(toCoordinate.getLongitude());

        // Haversine formula
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance in kilometers
        double distanceKm = EARTH_RADIUS_KM * c;

        return distanceKm;
    }

}
