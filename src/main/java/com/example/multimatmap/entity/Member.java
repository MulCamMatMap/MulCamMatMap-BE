package com.example.multimatmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;
    
    @Column
    private String role; //관리자/일반 사용자 권한설정

    public static Member createAdmin(String email, String username, String password, String role) {
        Member admin = new Member();  // 이 줄은 클래스 내부이므로 protected 생성자 사용 가능
        admin.setEmail(email);
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole(role);
        return admin;
    }

}
