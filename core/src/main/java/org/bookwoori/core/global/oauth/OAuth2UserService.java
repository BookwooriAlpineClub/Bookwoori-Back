package org.bookwoori.core.global.oauth;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.bookwoori.core.domain.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        // 2. registrationId 가져오기 (third-party id)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        // 4. 유저 정보 dto 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.ofKakao(oAuth2UserAttributes);
        // 5. 회원가입 및 로그인
        Member member = getOrSave(oAuth2UserInfo, (Long) oAuth2UserAttributes.get("id"));  // 카카오 id로 사용자 판별
        // 6. OAuth2User로 반환
        return new PrincipalDetails(oAuth2UserAttributes, userNameAttributeName, member);
    }

    private Member getOrSave(OAuth2UserInfo oAuth2UserInfo, Long kakaoId) {
        // 카카오 id를 사용하여 회원 찾기 또는 저장
        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> memberRepository.save(oAuth2UserInfo.toEntityWithKakaoId(kakaoId)));
        return member;
    }
}
