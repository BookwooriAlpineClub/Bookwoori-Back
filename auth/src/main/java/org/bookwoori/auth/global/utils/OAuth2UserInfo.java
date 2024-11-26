package org.bookwoori.auth.global.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OAuth2UserInfo {

    @Autowired
    private RestTemplate restTemplate;

    String nickname;
    String profile;

    // 카카오 사용자 정보를 기반으로 OAuth2UserInfo 생성
    public static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
            .nickname(generateRandomNickname())
            .profile((String) profile.get("profile_image_url"))
            .build();
    }

    // 랜덤 닉네임 생성 메소드
    private static String generateRandomNickname() {
        String[] words = {"솔바람", "별빛", "이슬", "물안개", "숲길", "산새", "구름", "노을빛", "푸른숲", "종이달"};
        int randomIndex = new Random().nextInt(words.length);
        String uuidPart = UUID.randomUUID().toString().substring(0, 4);
        return words[randomIndex] + uuidPart;
    }
}
