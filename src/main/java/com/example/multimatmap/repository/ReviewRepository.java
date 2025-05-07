package com.example.multimatmap.repository;

import com.example.multimatmap.entity.Member;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long id);
    List<Review> findAllByRestaurantId(Long restaurantId);

    @Query("SELECT AVG(r.score) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double findAverageScoreByRestaurantId(@Param("restaurantId") Long restaurantId);

    boolean existsByMemberAndRestaurant(Member member, Restaurant restaurant);
}
