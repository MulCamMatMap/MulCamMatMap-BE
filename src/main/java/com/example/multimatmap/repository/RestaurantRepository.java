package com.example.multimatmap.repository;

import com.example.multimatmap.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByNameAndAddress(String name, String address);
    List<Restaurant> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng, double maxLng);

    boolean existsBySlackTs(String slackTs);

}