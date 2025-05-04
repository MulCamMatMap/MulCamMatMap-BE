package com.example.multimatmap.controller;


import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.service.CommentService;
import com.example.multimatmap.service.RestaurantService;
import com.example.multimatmap.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final RestaurantService restaurantService;
    private final CommentService commentService;

    @PostMapping
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.save(restaurant);
    }
}
