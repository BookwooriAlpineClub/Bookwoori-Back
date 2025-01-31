package org.bookwoori.core.domain.member.service;

import org.bookwoori.core.domain.member.entity.Member;

public interface MemberService {

  Member getMemberById(Long memberId);

  Member getCurrentMember();

  Member getMemberByKakaoId(Long kakaoId);

  void validateMemberStatus(Long kakaoId);

  void saveMember(Member member);

  boolean existsByKakaoId(Long kakaoId);

  boolean existsByNickname(String newNickname);

}
