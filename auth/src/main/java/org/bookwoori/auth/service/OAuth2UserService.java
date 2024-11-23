package org.bookwoori.auth.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.auth.dto.GetMemberResponseDto;
import org.bookwoori.auth.dto.GetOrSaveMemberRequestDto;
import org.bookwoori.auth.feignClient.CoreClient;
import org.bookwoori.auth.utils.OAuth2UserInfo;
import org.bookwoori.auth.utils.PrincipalDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final CoreClient coreClient;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        // registrationId 가져오기 (third-party id)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();
        // 유저 정보 dto 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.ofKakao(oAuth2UserAttributes);
        Long kakaoId = (Long) oAuth2UserAttributes.get("id"); // 카카오 id

        // 회원 조회 및 생성 요청 한번에 처리
        GetOrSaveMemberRequestDto requestDto = new GetOrSaveMemberRequestDto(kakaoId,
            oAuth2UserInfo.getNickname(), oAuth2UserInfo.getProfile());
        GetMemberResponseDto responseDto = coreClient.getOrSaveMember(requestDto).getBody();
        // OAuth2User로 반환
        return new PrincipalDetails(oAuth2UserAttributes, userNameAttributeName, responseDto);
    }
}


