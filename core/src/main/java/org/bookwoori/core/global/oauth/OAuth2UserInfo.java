package org.bookwoori.core.global.oauth;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Builder
public record OAuth2UserInfo(
        String nickname,
        String profile
) {

    // 카카오 사용자 정보를 기반으로 OAuth2UserInfo 생성
    public static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .nickname(generateRandomNickname())
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    // 카카오 id를 포함하여 Member 엔티티로 변환
    public Member toEntityWithKakaoId(Long kakaoId) {
        return Member.builder()
                .kakaoId(kakaoId)  // 고유한 카카오 id 설정
                .nickname(nickname)
                .profileImg(profile)
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

