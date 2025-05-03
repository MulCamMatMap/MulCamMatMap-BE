package com.example.multimatmap.controller;

import com.example.multimatmap.dto.RestaurantDTO;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.service.RestaurantService;
import com.example.multimatmap.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final SlackService slackService;

    @PostMapping
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.save(restaurant);
    }

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @GetMapping("/restaurants/in-bounds")
    public List<RestaurantDTO> getRestaurantsInBounds(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng) {
        return restaurantService.findInBounds(minLat, maxLat, minLng, maxLng);
    }

    @GetMapping("/within")
    public List<RestaurantDTO> getRestaurantsWithin(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng
    ) {
        return restaurantService.findWithinBounds(minLat, maxLat, minLng, maxLng);
    }

    @PostMapping("/fetch-from-slack")
    public String fetchFromSlack() {
        slackService.fetchAndSaveMessages();
        return "슬랙 데이터 가져오기 완료";
    }

    @DeleteMapping("/admin/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}