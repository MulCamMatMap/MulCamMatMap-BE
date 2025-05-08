package com.example.multimatmap.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

@SpringBootTest
class CategoryCheckServiceTest {

    @Autowired
    private CategoryCheckService categoryCheckService;

    @Test
    void testIsRestaurant_Sanggu() {
        String name = "상구네솥뚜껑삼겹살 화양점";
        String link = "https://naver.me/GrmaZG9f";

        boolean result = categoryCheckService.isRestaurant(name);
        System.out.println("✅ 상구네솥뚜껑삼겹살 화양점 => 음식점 여부: " + result);
    }

    @Test
    void testIsRestaurant_Asheim() {
        String name = "아스하임";
        String link = "https://naver.me/FO9LfY6E";

        boolean result = categoryCheckService.isRestaurant(name);
        System.out.println("✅ 아스하임 => 음식점 여부: " + result);
    }

    @Test
    void testIsRestaurant_haehwa() {
        String name = "해화로";
        String link = "https://naver.me/FO9LfY6E";

        boolean result = categoryCheckService.isRestaurant(name);
        System.out.println("✅ 혜화로 => 음식점 여부: " + result);
    }

    @Test
    void testIsRestaurant() {
        String name = "세종대학교 학생회관 푸드코트";
        
        name += " 광진구";

        boolean result = categoryCheckService.isRestaurant(name);
        System.out.println("✅ " + name + " => 음식점 여부: " + result);
    }
}
