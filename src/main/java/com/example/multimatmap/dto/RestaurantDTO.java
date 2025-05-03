package com.example.multimatmap.dto;

import com.example.multimatmap.entity.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String link;
    private String note;
    private double latitude;
    private double longitude;
    private List<String> categories; // 식당에 해당하는 카테고리 정보

    public RestaurantDTO(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.link = restaurant.getLink();
        this.note = restaurant.getNote();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.categories = restaurant.getCategoryRestaurants().stream()
                .map(categoryRestaurant -> categoryRestaurant.getCategory().getName())
                .collect(Collectors.toList());
    }

}
