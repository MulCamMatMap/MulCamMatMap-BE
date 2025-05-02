package com.example.multimatmap.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverGeocodeResponse {
    private List<Address> addresses;

    @Getter
    public static class Address {
        private String x; // 경도
        private String y; // 위도
    }

    public Coordinate getFirstCoordinate() {
        if (addresses != null && !addresses.isEmpty()) {
            Address first = addresses.get(0);
            return new Coordinate(Double.parseDouble(first.getY()), Double.parseDouble(first.getX()));
        }
        return null;
    }
}