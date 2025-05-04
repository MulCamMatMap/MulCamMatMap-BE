package com.example.multimatmap.service;


import com.example.multimatmap.dto.CommentDTO;
import com.example.multimatmap.dto.CommentResponseDTO;
import com.example.multimatmap.entity.Comment;
import com.example.multimatmap.entity.Member;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.repository.CommentRepository;
import com.example.multimatmap.repository.MemberRepository;
import com.example.multimatmap.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    /*
    댓글을 남기는 Service 메서드.
     */
    public CommentResponseDTO addComment(CommentDTO commentDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        //오 이걸루 JWTtokenProvider 사용가능.
        Member member= memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
        Restaurant restaurant= restaurantRepository.findById(commentDTO.getRestaurantId())
                .orElseThrow(()-> new IllegalArgumentException("해당 식당을 찾을 수 없습니다."));
        Comment comment= Comment.builder()
                .content(commentDTO.getContent())
                .member(member)
                .restaurant(restaurant)
                .build();

        Comment saved = commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .memberName(member.getUsername())
                .restaurantId(restaurant.getId())
                .build();
    }

    //해당하는 Restaurant id 로 댓글 목록들을 조회할 수 있는 메서드.
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getComments(Long restaurantId) {
        return commentRepository.findByRestaurantId(restaurantId).stream()
                .map(comment -> CommentResponseDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .memberName(comment.getMember().getUsername())
                        .restaurantId(comment.getRestaurant().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
