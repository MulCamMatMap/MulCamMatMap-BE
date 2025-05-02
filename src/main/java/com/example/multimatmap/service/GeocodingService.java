package com.example.multimatmap.service;

import com.example.multimatmap.dto.Coordinate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate;

    private static final String GEOCODING_API_URL = "https://maps.apigw.ntruss.com/map-geocode/v2/geocode";

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String cleanAddress(String address) {
        // 괄호 제거
        address = address.replaceAll("\\(.*?\\)", "");
        // 호수, 층수, 건물명 제거
        address = address.replaceAll("\\s*(\\d+호|\\d+층|지하\\d+층|\\d+동\\s*\\d+호|\\d+동|\\d+층).*", "");
        // 앞뒤 공백 정리
        return address.trim();
    }

    // 📦 응답에서 좌표 추출
    public Coordinate extractCoordinate(String responseBody, String query) {
        JSONObject json = new JSONObject(responseBody);

        if (!"OK".equals(json.optString("status"))) {
            throw new IllegalStateException("API 요청 실패: " + query + " → " + json.optString("errorMessage"));
        }

        JSONArray addresses = json.optJSONArray("addresses");
        if (addresses == null || addresses.isEmpty()) {
            throw new IllegalStateException("응답에 주소가 없음: " + query);
        }

        JSONObject coord = addresses.getJSONObject(0);
        double x = coord.getDouble("x"); // 경도
        double y = coord.getDouble("y"); // 위도
        return new Coordinate(y, x);
    }

    // 🌍 네이버 API 호출 (장소명 or 주소)
    public Coordinate getCoordinates(String addressOrName) {
        String cleaned = cleanAddress(addressOrName);
        System.out.println("➡️ 전처리 후 주소: " + cleaned);
        String encodedQuery = UriUtils.encodeQueryParam(cleaned, StandardCharsets.UTF_8);
        String url = GEOCODING_API_URL + "?query=" + cleaned;
//        String url = GEOCODING_API_URL + "?query=" + encodedQuery;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id", clientId);
        headers.set("x-ncp-apigw-api-key", clientSecret);
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            System.out.println("➡️ 요청 URL: " + url);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );
            System.out.println("📦 응답: " + response.getBody());

            return extractCoordinate(response.getBody(), cleaned);
        } catch (Exception e) {
            System.err.println("❌ 좌표 변환 실패: " + addressOrName);
            e.printStackTrace();
            return null;
        }
    }
}