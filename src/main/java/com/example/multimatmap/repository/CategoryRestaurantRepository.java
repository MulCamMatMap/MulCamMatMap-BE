package com.example.multimatmap.repository;

import com.example.multimatmap.entity.CategoryRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRestaurantRepository extends JpaRepository<CategoryRestaurant, Long> {
    Optional<CategoryRestaurant> findById(Long id);
}
