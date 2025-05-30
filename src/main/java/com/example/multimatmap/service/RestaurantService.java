package com.example.multimatmap.service;

import com.example.multimatmap.dto.RestaurantDTO;
import com.example.multimatmap.entity.Category;
import com.example.multimatmap.entity.CategoryRestaurant;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.repository.CategoryRepository;
import com.example.multimatmap.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 식당 정보를 저장
     * @param restaurant
     * @return
     */
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    /**
     * 카테고리 매핑 테이블 까지 만들어서 저장하는 로직
     * - 기존에 존재하지 않는 카테고리를 새로 생성
     * - 식당과 카테고리 간의 M:N 관계 설정
     */
    public void saveCategories(Restaurant restaurant, List<String> categoryNames) {
        restaurant.getCategoryRestaurants().clear();

        for (String name : categoryNames) {
            Category category = categoryRepository.findByName(name)
                    .orElseGet(() -> categoryRepository.save(new Category(name)));

            CategoryRestaurant cr = CategoryRestaurant.builder()
                    .restaurant(restaurant)
                    .category(category)
                    .build();

            restaurant.getCategoryRestaurants().add(cr); // set이 아니라 add!
        }

        // 3. save
        restaurantRepository.save(restaurant);
    }
    /**
     * 식당이 존재하지 않을 경우에만 저장
     * @param restaurant
     */
    public void saveUnique(Restaurant restaurant) {
        if (!restaurantRepository.existsByNameAndAddress(restaurant.getName(), restaurant.getAddress())) {
            restaurantRepository.save(restaurant);
        }
    }

    /**
     * 전체 식당 목록을 조회하여 DTO로 변환 후 반환
     * @return
     */
    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 이름과 주소를 가진 식당이 존재하는지 확인
     * @param name
     * @param address
     * @return 존재 여부
     */
    public boolean existsByNameAndAddress(String name, String address) {
        return restaurantRepository.existsByNameAndAddress(name, address);
    }

    /**
     * 주어진 위도/경도 범위 안에 포함되는 식당들을 조회
     * @param minLat
     * @param maxLat
     * @param minLng
     * @param maxLng
     * @return
     */
    public List<RestaurantDTO> findInBounds(double minLat, double maxLat, double minLng, double maxLng) {
        return restaurantRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng).stream()
                .map(RestaurantDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 위도/경도 범위 안에 포함되는 식당들을 조회 (나중에 삭제)
     * @param minLat
     * @param maxLat
     * @param minLng
     * @param maxLng
     * @return
     */
    public List<RestaurantDTO> findWithinBounds(double minLat, double maxLat, double minLng, double maxLng) {
        return restaurantRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng).stream()
                .map(RestaurantDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 식당 ID로 삭제, 존재하지 않을 경우 예외 발생
     * @param id
     */
    public void deleteById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        restaurantRepository.delete(restaurant);
    }

    /**
     * 식당 id를 이용하여 식당 정보 update, 존재하지 않을 경우 예외 발생
     * @param id
     * @param restaurant
     * @return
     */
    @Transactional
    public Restaurant updateById(Long id, Restaurant restaurant) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        existingRestaurant.updateRestaurantDetails(restaurant);
        return restaurantRepository.save(existingRestaurant);
    }
}