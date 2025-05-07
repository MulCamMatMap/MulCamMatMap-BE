package com.example.multimatmap.dto;

import com.example.multimatmap.entity.Member;
import com.example.multimatmap.entity.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * request 인자로 restaurantId 요구
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReviewRequestDTO {
    private Long id;
    private String content;
    private Integer score;
    private Long restaurantId;
}
