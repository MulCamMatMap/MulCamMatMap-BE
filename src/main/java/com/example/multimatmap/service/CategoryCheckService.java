package com.example.multimatmap.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * 네이버 지역 검색 API를 사용하여 입력된 장소가 음식점인지 판별하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CategoryCheckService {

    private final RestTemplate restTemplate;

    private static final String SEARCH_LOCAL_URL = "https://openapi.naver.com/v1/search/local.json";

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    /**
     * 입력된 쿼리(장소명 또는 주소)가 음식점 관련 카테고리에 포함되는지 확인
     */
    public boolean isRestaurant(String query) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(SEARCH_LOCAL_URL)
                    .queryParam("query", query)
                    .queryParam("display", 1)
                    .build()
                    .toUriString();

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

            if (items != null && !items.isEmpty()) {
                JSONObject item = items.getJSONObject(0);
                String category = item.optString("category");
                return category.contains("음식점") || category.contains("한식") || category.contains("카페");
            }
        } catch (Exception e) {
            System.err.println("❌ 지역 검색 실패: " + e.getMessage());
        }
        return false;
    }
}
