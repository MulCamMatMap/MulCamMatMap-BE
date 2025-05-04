package com.example.multimatmap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//Request DTO 역할. 해당 정보를 요구합니다.
@Getter
@AllArgsConstructor
public class CommentDTO {
    private String content;
    private Long restaurantId;
}
