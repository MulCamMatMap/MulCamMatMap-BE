package com.example.multimatmap.controller;

import com.example.multimatmap.config.JwtTokenProvider;
import com.example.multimatmap.dto.MemberLoginDTO;
import com.example.multimatmap.dto.MemberSignDTO;
import com.example.multimatmap.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> Signup(@RequestBody @Valid MemberSignDTO signrequest){
        memberService.signup(signrequest);
        return ResponseEntity.ok("회원가입이 완료 되었습니다.");
    }

    //로그인 하고, 토큰을 받아옴
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid MemberLoginDTO loginrequest){
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String accessToken = memberService.login(loginrequest);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7)) // 리프레시 토큰 유효기간 (7일)
                .build();

        Map<String, String> responseBody = Map.of("accessToken", accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(responseBody.toString());
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        // HTTPOnly 쿠키에서 리프레시 토큰을 가져옴
        String refreshToken = jwtTokenProvider.refreshToken(request);

        // 리프레시 토큰이 유효한지 검사
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            // 리프레시 토큰이 유효하면 새로운 액세스 토큰을 발급
            String email = jwtTokenProvider.getUserName(refreshToken);
            String role = "ROLE_USER"; //일단 역할은 유저로
            String newAccessToken = jwtTokenProvider.createToken(email, role);
            // 새로운 액세스 토큰을 반환
            return ResponseEntity.ok(newAccessToken);
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @GetMapping("/check-role")
    public ResponseEntity<?> checkRole(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities());
    }
}
