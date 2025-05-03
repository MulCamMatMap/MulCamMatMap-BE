package com.example.multimatmap.service;

import com.example.multimatmap.dto.Coordinate;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {

    private final RestaurantRepository restaurantRepository;
    private final GeocodingService geocodingService; // 좌표 변환
    private final RestaurantService restaurantService;
    private List<String> parsedCategories = new ArrayList<>();

    @Value("${slack.token}")
    private String token;

    @Value("${slack.channel-id}")
    private String channelId;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://slack.com/api")
            .build();

    public void fetchAndSaveMessages() {
        String url = "/conversations.history?channel=" + channelId + "&limit=1000";

        List<SlackMessage> messages = webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(SlackResponse.class)
                .map(SlackResponse::getMessages)
                .block();

        if (messages == null) {
            log.error("Slack에서 메시지를 가져오지 못했습니다.");
            return;
        }

        int savedCount = 0;

        for (SlackMessage message : messages) {
            if (!message.getText().contains(":pushpin:")) continue;
            if (restaurantRepository.existsBySlackTs(message.getTs())) {
                continue; // 이미 저장된 메시지면 skip
            }

            Restaurant restaurant = parseMessage(message.getText());
            if (restaurant != null) {
                restaurant.setSlackTs(message.getTs()); // ts 저장
                restaurantRepository.save(restaurant);
                restaurantService.saveCategories(restaurant, parsedCategories);
                savedCount++;
            }
        }

        log.info("✅ 저장된 레스토랑 수: {}", savedCount);
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

        if (!name.find() || !location.find()) {
            log.warn("❗ 메시지 파싱 실패: {}", text);
            return null;
        }

        String cleanAddress = location.group(1).trim();
        Coordinate coord = geocodingService.getCoordinates(cleanAddress);

        if (coord == null) {
            log.warn("❗ 좌표 변환 실패: {}", cleanAddress);
            return null;
        }

        String categoryRaw = type.find() ? type.group(1).trim() : "";
        parsedCategories = parseCategoryNames(categoryRaw);

        return Restaurant.builder()
                .name(name.group(1).trim())
//                .category(type.find() ? type.group(1).trim() : "")
                .address(cleanAddress)
                .link(link.find() ? link.group(1).trim() : "")
                .note(note.find() ? note.group(1).trim() : "")
                .latitude(coord.getLatitude())
                .longitude(coord.getLongitude())
                .build();
    }

    // 슬랙 응답용 내부 클래스
    static class SlackResponse {
        private List<SlackMessage> messages;

        public List<SlackMessage> getMessages() {
            return messages;
        }
    }

    static class SlackMessage {
        private String text;
        private String ts;  // 슬랙 메시지 고유 ID

        public String getText() {
            return text;
        }

        public String getTs() {
            return ts;
        }
    }

    private List<String> parseCategoryNames(String categoryString) {
        List<String> result = new ArrayList<>();
        if (categoryString != null && !categoryString.trim().isEmpty()) {
            String[] names = categoryString.split(",");
            for (String name : names) {
                result.add(name.trim());
            }
        }
        return result;
    }
}