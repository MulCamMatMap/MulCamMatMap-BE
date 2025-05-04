package com.example.multimatmap.controller;

import com.example.multimatmap.dto.RestaurantDTO;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateRestaurantTest() throws Exception {
        // 초기 데이터 설정: 테스트용 식당을 저장
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .address("Test Address")
                .link("http://test-link.com")
                .note("Test Note")
                .latitude(37.0)
                .longitude(127.0)
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // 저장된 ID 확인
        System.out.println("Saved restaurant ID: " + restaurant.getId());

        // 업데이트용 DTO 생성
        RestaurantDTO updateDTO = new RestaurantDTO();
        updateDTO.setName("Updated Restaurant");
        updateDTO.setAddress("Updated Address");
        updateDTO.setLink("http://updated-link.com");
        updateDTO.setNote("Updated Note");
        updateDTO.setLatitude(38.0);
        updateDTO.setLongitude(128.0);

        // DTO를 JSON으로 변환하여 확인
        String jsonContent = objectMapper.writeValueAsString(updateDTO);
        System.out.println("Update JSON payload: " + jsonContent);

        // PUT 요청을 통해 업데이트 실행 및 결과 검증
        mockMvc.perform(put("/restaurants/admin/" + restaurant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Restaurant")))
                .andExpect(jsonPath("$.address", is("Updated Address")))
                .andExpect(jsonPath("$.link", is("http://updated-link.com")))
                .andExpect(jsonPath("$.note", is("Updated Note")))
                .andExpect(jsonPath("$.latitude", is(38.0)))
                .andExpect(jsonPath("$.longitude", is(128.0)));
    }

}
