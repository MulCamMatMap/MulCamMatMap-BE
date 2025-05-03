package com.example.multimatmap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;     // 식당명
    private String address;  // 위치
    private String link;     // 링크

    @Column(columnDefinition = "TEXT")
    private String note;     // 비고 (nullable 가능)

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    private String slackTs;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryRestaurant> categoryRestaurants = new ArrayList<>();

    public void updateRestaurantDetails(Restaurant restaurant) {
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.note = restaurant.getNote();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
    }
}