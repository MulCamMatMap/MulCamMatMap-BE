package com.example.multimatmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private String content;
    private Integer score;
    private Long restaurantId;
    private String memberName;
}
