package com.example.commonlib.utils;

import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

public class DistanceUtils {

    private static final int EARTH_RADIUS_KM = 6371;

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS_KM * c;
    }


    public static Map<String, Double> getLatitudeLongitude(Authentication authentication) {
        Object details = authentication.getDetails();
        HashMap<String, Double> latitudeLongitude = new HashMap<>();
        Double latitude = null;
        Double longitude = null;

        if (details instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) details;
            latitude = (Double) map.get("latitude");
            longitude = (Double) map.get("longitude");
        }

        if(latitude == null || longitude == null) {
            throw new RuntimeException("latitude or longitude is null");
        }

        latitudeLongitude.put("latitude", latitude);
        latitudeLongitude.put("longitude", longitude);

        return latitudeLongitude;
    }
}
