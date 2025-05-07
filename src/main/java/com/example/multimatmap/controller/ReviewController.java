package com.example.multimatmap.controller;

import com.example.multimatmap.dto.CommentDTO;
import com.example.multimatmap.dto.CommentResponseDTO;
import com.example.multimatmap.dto.ReviewRequestDTO;
import com.example.multimatmap.dto.ReviewResponseDTO;
import com.example.multimatmap.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> addReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewResponseDTO responseDTO = reviewService.addReview(reviewRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<ReviewResponseDTO>> getComments(@PathVariable Long restaurantId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviews(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/restaurants/{restaurantId}/score")
    public Double getScore(@PathVariable Long restaurantId) {
        Double avgScore = reviewService.getScore(restaurantId);
        return avgScore;
    }
}
