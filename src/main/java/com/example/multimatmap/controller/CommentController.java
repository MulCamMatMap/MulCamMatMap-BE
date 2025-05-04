package com.example.multimatmap.controller;


import com.example.multimatmap.dto.CommentDTO;
import com.example.multimatmap.dto.CommentResponseDTO;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.service.CommentService;
import com.example.multimatmap.service.RestaurantService;
import com.example.multimatmap.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final RestaurantService restaurantService;
    private final CommentService commentService;

    /*
    /comment 로 POST 에 Requestbody 만 넘겨주면 댓글 등록 가능.
     */
    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentDTO commentDTO) {
        CommentResponseDTO responseDTO = commentService.addComment(commentDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 특정 restautantId 로 해당 댓글들 조회가능.
     * @param restaurantId
     * @return
     */
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Long restaurantId) {
        List<CommentResponseDTO> comments = commentService.getComments(restaurantId);
        return ResponseEntity.ok(comments);
    }
}
