package com.example.multimatmap.config;

import com.example.multimatmap.dto.Coordinate;
import com.example.multimatmap.entity.Category;
import com.example.multimatmap.entity.CategoryRestaurant;
import com.example.multimatmap.entity.Restaurant;
import com.example.multimatmap.repository.CategoryRepository;
import com.example.multimatmap.repository.RestaurantRepository;
import com.example.multimatmap.service.GeocodingService;
import com.example.multimatmap.service.RestaurantService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final RestaurantService restaurantService;
    private final GeocodingService geocodingService;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    @PostConstruct
    public void init() {
        saveUnique("해화로", "일식", "서울 광진구 광나루로 383 1,2층", "https://naver.me/5Q3VgeaD", "점심특선 고등어구이🐟👍");
        saveUnique("행복한그릇", "일식", "서울 광진구 광나루로17길 24-8", "https://naver.me/Fw7iIa0E", "가성비 좋은 세종대 학생들에게 인기 많은 덮밥집");
        saveUnique("미태리 파스타", "양식", "서울 광진구 광나루로 380 1층", "https://naver.me/GYC9jFtY", "가성비, 나름 분위기 좋은 파스타집");
        saveUnique("스위트앤카츠", "일식", "서울 광진구 광나루로17길 14", "https://naver.me/xWTnbo9S", "돈까스무한리필뷔페👍");
        saveUnique("고을칼국수", "분식", "서울 광진구 광나루로17길 14-5", "https://naver.me/GiphLR1d", "면부터 직접 뽑는 곳");
        saveUnique("마가을고기", "한식", "서울 광진구 광나루로 381-1", "https://naver.me/FK5QmIPC", "가성비 좋은 한식 뷔페(점심시간에만 뷔페운영)");
        saveUnique("세종대학교 푸드코트(학식)", "한식,분식", "서울 광진구 능동로 209", "https://naver.me/IGJIHvI5", "가성비 최고, 학식만 먹으러 오기도 하는 유명한 학식👍");
        saveUnique("건대우동집 어린이대공원점", "분식", "서울 광진구 광나루로 386 하이뷰오피스텔 102호", "https://naver.me/FxFHRwVI", "자리는 좁지만, 회전이 빨라요!");
        saveUnique("은혜즉석떡볶이", "분식", "서울 광진구 광나루로 381-1", "https://naver.me/xxY28m4Z", "짜장 즉석떡볶이🍽️");
        saveUnique("철순이네 김치찌개", "한식", "서울 광진구 광나루로 361 동양파라곤 101동 1층 철순이네김치찌개", "https://naver.me/5MVOTsH5", "김치돼지볶음도 맛👍");
        saveUnique("카레당", "일식", "서울 광진구 능동로 251", "https://naver.me/FbOnQGUG", "웨이팅주의, 밥/카레 리필 가능👍");
        saveUnique("신사골감자탕 어린이대공원점", "한식", "서울 광진구 광나루로 380 1층", "https://naver.me/IDFUnjUk", "넓은 자리, 가격 저렴, 푸짐한 양");
        saveUnique("미식반점 군자본점", "중식", "서울 광진구 군자로 70 1층", "https://naver.me/xX7OPRbq", null);
        saveUnique("계절밥상 세종대학교점", "한식", "서울 광진구 능동로 209 군자관 6층", "https://naver.me/xmxAGO3d", "넓은 자리, 가격 저렴");
        saveUnique("멀캠 반점", "한식,중식,일식,양식", "서울 광진구 능동로 195-16 5층", "https://naver.me/xX7OPRbq", null);
        saveUnique("아무개 카페", "식당", "서울 마포구 동교동 164-8 1층", "https://naver.me/GALRciQ6", "치킨 먹고 싶다");
    }

    private void saveUnique(String name, String category, String address, String link, String note) {
        System.out.println("✅ 저장 시도 중: " + name + " - " + address);
        if (!restaurantService.existsByNameAndAddress(name, address)) {
            try {
                Coordinate coord = geocodingService.getCoordinates(address);
                if (coord != null) {
                    System.out.println("📍 변환된 좌표: " + coord.getLatitude() + ", " + coord.getLongitude());
                    Restaurant restaurant = Restaurant.builder()
                            .id(null)
                            .name(name)
                            .address(address)
                            .link(link)
                            .note(note)
                            .latitude(coord.getLatitude())
                            .longitude(coord.getLongitude())
                            .build();
                    restaurantService.save(restaurant);

                    List<String> categoryNames =parseCategoryNames(category) ;
                    restaurantService.saveCategories(restaurant, categoryNames);
                } else {
                    System.err.println("❗ 좌표를 찾을 수 없습니다: " + address);
                }
            } catch (Exception e) {
                System.err.println("❌ 주소 변환 실패: " + address);
                e.printStackTrace();
            }
        }
    }

    //카테고리 여러개인 경우 저장
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