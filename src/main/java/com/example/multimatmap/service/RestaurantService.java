package com.example.multimatmap.service;

import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void saveUnique(Restaurant restaurant) {
        if (!restaurantRepository.existsByNameAndAddress(restaurant.getName(), restaurant.getAddress())) {
            restaurantRepository.save(restaurant);
        }
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public boolean existsByNameAndAddress(String name, String address) {
        return restaurantRepository.existsByNameAndAddress(name, address);
    }

    public List<Restaurant> findInBounds(double minLat, double maxLat, double minLng, double maxLng) {
        return restaurantRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng);
    }

    public List<Restaurant> findWithinBounds(double minLat, double maxLat, double minLng, double maxLng) {
        return restaurantRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng);
    }


    // id 값을 이용하여 식당 삭제
    public void DeleteRestaurantById(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new IllegalArgumentException("Restaurant with id " + id + " does not exist");
        }
        restaurantRepository.deleteById(id);
    }

    // 식당 수정
    @Transactional  // 변경 감지(dirty checking)
    public void updateRestaurant(Restaurant restaurant) {
        Restaurant existRestaurant = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(()-> new IllegalArgumentException("Restaurant with id " + restaurant.getId() + " does not exist"));

        existRestaurant.updateFrom(restaurant);
    }
}