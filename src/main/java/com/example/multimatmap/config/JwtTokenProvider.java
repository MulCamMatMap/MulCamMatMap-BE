package com.example.multimatmap.config;

import com.example.multimatmap.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

//토큰 생성용 클래스입니다. 지피티랑 친구먹었음;;
@Component
public class JwtTokenProvider {

    //yml 파일에서 코드들과 지속시간을 불러옵니다.
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds * 2); // 리프레시 토큰은 더 긴 유효 기간

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 이메일 추출
    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUserName(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    public Authentication getAuthentication(String token) {
        String userEmail = getEmail(token);
        return new UsernamePasswordAuthenticationToken(userEmail, token, null); // 권한은 null
    }

    // Request에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        // 1. Authorization 헤더 체크
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        //2.쿠키 체크
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}

