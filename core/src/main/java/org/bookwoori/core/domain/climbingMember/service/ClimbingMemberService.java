package org.bookwoori.core.domain.climbingMember.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbingMember.repository.ClimbingMemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClimbingMemberService {

    private final ClimbingMemberRepository climbingMemberRepository;
}
