package com.example.multimatmap.service;

import com.example.multimatmap.dto.CommentDTO;
import com.example.multimatmap.dto.CommentResponseDTO;
import com.example.multimatmap.dto.ReviewRequestDTO;
import com.example.multimatmap.dto.ReviewResponseDTO;
import com.example.multimatmap.entity.Comment;
import com.example.multimatmap.entity.Member;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.entity.Review;
import com.example.multimatmap.repository.CommentRepository;
import com.example.multimatmap.repository.MemberRepository;
import com.example.multimatmap.repository.RestaurantRepository;
import com.example.multimatmap.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * reviewRequestDTO 정보들을 받아 Review 를 작성합니다. (댓글과 비슷하나 score 존재.)
     * @param reviewRequestDTO
     * @return
     */
    public ReviewResponseDTO addReview(ReviewRequestDTO reviewRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        //오 이걸루 JWTtokenProvider 사용가능. -> 사용자의 정보를 얻을 수 있다.
        Member member= memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
        Restaurant restaurant= restaurantRepository.findById(reviewRequestDTO.getRestaurantId())
                .orElseThrow(()-> new IllegalArgumentException("해당 식당을 찾을 수 없습니다."));
        Review review= Review.builder()
                .content(reviewRequestDTO.getContent())
                .score(reviewRequestDTO.getScore())
                .member(member)
                .restaurant(restaurant)
                .build();

        Review saved = reviewRepository.save(review);

        return ReviewResponseDTO.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .score(saved.getScore())
                .memberName(member.getUsername())
                .restaurantId(restaurant.getId())
                .build();
    }

    /**
     * restaurantId 로 거기 달린 리뷰들을 조회.
     * @param restaurantId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviews(Long restaurantId) {
        return reviewRepository.findAllByRestaurantId(restaurantId).stream()
                .map(review -> ReviewResponseDTO.builder()
                        .id(review.getId())
                        .content(review.getContent())
                        .memberName(review.getMember().getUsername())
                        .score(review.getScore())
                        .restaurantId(review.getRestaurant().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
