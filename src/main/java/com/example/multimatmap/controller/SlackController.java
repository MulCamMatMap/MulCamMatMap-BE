package com.example.multimatmap.controller;

import com.example.multimatmap.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {

    private final SlackService slackService;

    @GetMapping("/fetch")
    public String fetchMessages() {
        slackService.fetchAndSaveMessages();
        return "슬랙 메시지 가져오기 시도 완료!";
    }
}