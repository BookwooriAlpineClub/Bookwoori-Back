package org.bookwoori.core.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member getOrCreateMemberByKakaoId(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId)
            .orElse(Member.builder()
                .kakaoId(kakaoId)
                .build());
    }

    // Access Token 저장 또는 업데이트
    @Transactional
    public void saveOrUpdateAccessToken(Member member, String accessToken) {
        member.updateAccessToken(accessToken);
        log.info("saveOrUpdateAccessToken 접근");
        log.info(accessToken);
        memberRepository.save(member);  // 업데이트된 토큰 저장
    }

}
