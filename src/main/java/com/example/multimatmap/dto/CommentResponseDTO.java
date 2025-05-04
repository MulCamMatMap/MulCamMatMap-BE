package com.example.multimatmap.dto;

import lombok.Builder;
import lombok.Getter;

//댓글 조회관련 ResponseDTO 역할.
@Getter
@Builder
public class CommentResponseDTO {
    private Long id;
    private String content;
    /*
    어차피 유저 이름으로 댓글을 보여줄 거 같아서
    memberNamem 으로 반환하게 만들었습니다.
     */
    private String memberName;
    private Long restaurantId;
}
