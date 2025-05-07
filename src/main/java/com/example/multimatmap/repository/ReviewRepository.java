package com.example.multimatmap.repository;

import com.example.multimatmap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long id);
    Optional<Review> findByRestaurantId(Long restaurantId);
}
