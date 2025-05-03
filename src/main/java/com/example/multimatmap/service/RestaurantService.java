package com.example.multimatmap.service;

import com.example.multimatmap.entity.Category;
import com.example.multimatmap.entity.CategoryRestaurant;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.repository.CategoryRepository;
import com.example.multimatmap.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    //카테고리 매핑 테이블 까지 만들어서 저장하는 로직
    public void saveCategories(Restaurant restaurant, List<String> categoryNames) {
        List<CategoryRestaurant> categoryRestaurants = new ArrayList<>();

        for (String name : categoryNames) {
            Category category = categoryRepository.findByName(name)
                    .orElseGet(() -> categoryRepository.save(new Category(name)));

            CategoryRestaurant cr = CategoryRestaurant.builder()
                    .restaurant(restaurant)
                    .category(category)
                    .build();
            categoryRestaurants.add(cr);
        }
        restaurant.setCategoryRestaurants(categoryRestaurants);
        restaurantRepository.save(restaurant);
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
}