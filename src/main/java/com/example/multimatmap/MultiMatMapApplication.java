package com.example.multimatmap;

import com.example.multimatmap.config.AdminConfig;
import com.example.multimatmap.entity.Member;
import com.example.multimatmap.repository.MemberRepository;
import lombok.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MultiMatMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiMatMapApplication.class, args);
    }
//    @Value("${admin.password}")
//    private String adminPassword;

    @Bean
    public CommandLineRunner createAdmin(MemberRepository memberRepository, PasswordEncoder encoder, AdminConfig adminConfig) {
        return args -> {
            if (!memberRepository.existsByEmail("admin@example.com")) {
                Member admin = Member.createAdmin("admin@example.com", "admin", encoder.encode(adminConfig.getPassword()), "ADMIN");
                memberRepository.save(admin);
            }
        };
    }

}
