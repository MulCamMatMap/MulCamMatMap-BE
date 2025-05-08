package com.example.multimatmap.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 네이버 지역 검색 API를 사용하여 입력된 장소가 음식점인지 판별하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CategoryCheckService {

    private final RestTemplate restTemplate;

    private static final String SEARCH_LOCAL_URL = "https://openapi.naver.com/v1/search/local.json";

    @Value("${X-NAVER_CLIENT_ID}")
    private String clientId;

    @Value("${X-NAVER_CLIENT_SECRET}")
    private String clientSecret;

    /**
     * Naver local API를 이용하여 식당 정보를 입력받았을 때 올바른 음식 카테고리에 존재하는지 확인하는 함수
     * @param restaurantName 식당 이름
     * @return 올바른 카테고리이면 true, 아니면 false
     */
    public boolean isRestaurant(String restaurantName) {
        try {
            String[] RestaurantCategories = {
                    "음식점",
                    "한식",
                    "중식",
                    "일식",
                    "양식",
                    "분식",
                    "카페,디저트",
                    "술집",
                    "치킨",
                    "패스트푸드",
                    "뷔페",
                    "베이커리"
            };

            String url = UriComponentsBuilder.fromHttpUrl(SEARCH_LOCAL_URL)
                    .queryParam("query", restaurantName + " 광진구")
                    .queryParam("display", 5)  // 최대값: 5
                    .build()
                    .toUriString();

            System.out.println("url : " + url);

            // naver Developer 인증
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );

            JSONObject json = new JSONObject(response.getBody());
            JSONArray items = json.optJSONArray("items");

            // console 출력용
            System.out.println("length: " + items.length());
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                System.out.println("📌 Item " + (i + 1) + ":");
                for (String key : item.keySet()) {
                    System.out.println("  " + key + " : " + item.get(key));
                }
                System.out.println();
            }

            System.out.println("json: " + json);

            if (!items.isEmpty()) {
                JSONObject item = items.getJSONObject(0);
                String category = item.optString("category");

                System.out.println("📌 검색어: " + restaurantName);
                System.out.println("📌 결과 카테고리: " + category);

                return Arrays.stream(RestaurantCategories).anyMatch(restaurantCategory -> restaurantCategory.equals(category.split(">")[0]));
//                return category.contains("음식점") || category.contains("한식") || category.contains("중식") || category.contains("일식");
            }
        } catch (Exception e) {
            System.err.println("❌ 지역 검색 실패: " + e.getMessage());
        }
        return false;
    }

}
