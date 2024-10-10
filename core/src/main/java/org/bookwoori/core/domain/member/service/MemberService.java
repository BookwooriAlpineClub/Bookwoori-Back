package org.bookwoori.core.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

}
