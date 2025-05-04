package com.example.multimatmap.dto;

import com.example.multimatmap.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String link;
    private String note;
    private double latitude;
    private double longitude;
    private List<String> categories; // 식당에 해당하는 카테고리 정보

    public RestaurantDTO() {
        // Jackson 직렬화 및 테스트 코드에서 사용하기 위해 필요
    }

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

    public Restaurant toEntity() {
        return Restaurant.builder()
                .name(this.name)
                .address(this.address)
                .link(this.link)
                .note(this.note)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
