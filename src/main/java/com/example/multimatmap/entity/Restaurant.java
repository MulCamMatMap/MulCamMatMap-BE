package com.example.multimatmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;     // 식당명
    private String category; // 분류
    private String address;  // 위치
    private String link;     // 링크

    @Column(columnDefinition = "TEXT")
    private String note;     // 비고 (nullable 가능)

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    private String slackTs;
}