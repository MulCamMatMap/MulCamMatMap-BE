package com.example.multimatmap.entity;

import jakarta.persistence.*;
import lombok.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String content;

    /*
    별점은 0점에서 5점만 등록가능합니다.
     */
    @Min(0)
    @Max(5)
    @Column
    private Integer score;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;
}
