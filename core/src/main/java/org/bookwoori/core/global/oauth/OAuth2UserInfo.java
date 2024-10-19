package org.bookwoori.core.global.oauth;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

import java.util.Map;

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
                .nickname((String) profile.get("nickname"))
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
}

