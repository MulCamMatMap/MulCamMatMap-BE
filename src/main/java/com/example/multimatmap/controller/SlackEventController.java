package com.example.multimatmap.controller;

import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.service.GeocodingService;
import com.example.multimatmap.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackEventController {

    private final RestaurantService restaurantService;
    private final GeocodingService geocodingService;

    @PostMapping("/events")
    public Map<String, Object> receiveSlackEvent(@RequestBody Map<String, Object> payload) {
        // Slack challenge 인증용
        if ("url_verification".equals(payload.get("type"))) {
            return Map.of("challenge", payload.get("challenge"));
        }

        log.info("📨 슬랙 이벤트 수신: {}", payload);

        Map<String, Object> event = (Map<String, Object>) payload.get("event");
        if (event != null && "message".equals(event.get("type"))) {
            String text = (String) event.get("text");

            if (text != null && text.contains(":pushpin:")) {
                Restaurant restaurant = parseMessage(text);
                if (restaurant != null) {
                    try {
                        var coord = geocodingService.getCoordinates(restaurant.getAddress());
                        if (coord != null) {
                            restaurant.setLatitude(coord.getLatitude());
                            restaurant.setLongitude(coord.getLongitude());
                        }
                        restaurantService.save(restaurant);
                        log.info("✅ 식당 저장 성공: {}", restaurant.getName());
                    } catch (Exception e) {
                        log.error("❌ 저장 실패: {}", e.getMessage(), e);
                    }
                }
            }
        }
        return Map.of("status", "ok");
    }

    private Restaurant parseMessage(String text) {
        Pattern namePattern = Pattern.compile("식당명[:：]\\s*(.*)");
        Pattern typePattern = Pattern.compile("1[).．]\\s*분류[:：]\\s*(.*)");
        Pattern locationPattern = Pattern.compile("2[).．]\\s*위치[:：]\\s*(.*)");
        Pattern linkPattern = Pattern.compile("3[).．]\\s*링크[:：]\\s*<?(https?://[^\s>]+)>?");
        Pattern notePattern = Pattern.compile("4[).．]\\s*비고[:：]\\s*(.*)");

        Matcher name = namePattern.matcher(text);
        Matcher type = typePattern.matcher(text);
        Matcher location = locationPattern.matcher(text);
        Matcher link = linkPattern.matcher(text);
        Matcher note = notePattern.matcher(text);

        return Restaurant.builder()
                .name(name.find() ? name.group(1).trim() : "")
                .category(type.find() ? type.group(1).trim() : "")
                .address(location.find() ? location.group(1).trim() : "")
                .link(link.find() ? link.group(1).trim() : "")
                .note(note.find() ? note.group(1).trim() : "")
                .latitude(0.0)
                .longitude(0.0)
                .build();
    }
}