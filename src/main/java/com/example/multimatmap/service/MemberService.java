package com.example.multimatmap.service;

import com.example.multimatmap.config.JwtTokenProvider;
import com.example.multimatmap.dto.MemberLoginDTO;
import com.example.multimatmap.dto.MemberSignDTO;
import com.example.multimatmap.entity.Member;
import com.example.multimatmap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입 로직과 예외 처리
    public Member signup(MemberSignDTO memberSignDTO) {
        if(memberRepository.existsByEmail(String.valueOf(memberSignDTO.getEmail()))){
            throw new IllegalArgumentException("이미 회원이 존재하는 이메일 입니다.");
        }

        Member member= Member.builder()
                .email(String.valueOf(memberSignDTO.getEmail()))
                .password(passwordEncoder.encode(memberSignDTO.getPassword()))
                .username(memberSignDTO.getName())
                .role("USER")
                .build();

        return memberRepository.save(member);
    }

    //로그인 로직과 예외처리
    public String login(MemberLoginDTO memberLoginDTO) {
        Member member= memberRepository.findByEmail(memberLoginDTO.getEmail())
                .orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(memberLoginDTO.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }
        return jwtTokenProvider.createToken(member.getEmail(), member.getRole());
    }
    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
