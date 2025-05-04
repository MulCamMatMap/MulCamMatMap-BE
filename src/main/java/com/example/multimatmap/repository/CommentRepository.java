package com.example.multimatmap.repository;

import com.example.multimatmap.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);

    Optional<Comment> findByRestaurantId(Long restaurantId);
}
