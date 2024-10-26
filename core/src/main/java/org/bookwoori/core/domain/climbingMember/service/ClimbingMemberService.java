package org.bookwoori.core.domain.climbingMember.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.dto.ClimbingMemberDto;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.repository.ClimbingMemberRepository;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.stereotype.Service;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ClimbingMemberService {
    private final ClimbingMemberRepository climbingMemberRepository;

    public void saveMember(Member currentMember, Climbing climbing, ClimbingRole climbingRole) {
        boolean isJoined = climbingMemberRepository.findByMemberAndClimbing(currentMember, climbing).isPresent();
        if (isJoined) {
            throw new CustomException(ErrorCode.ALREADY_JOINED_CLIMBING);
        }

        ClimbingMember climbingMember = new ClimbingMember(
                currentMember,
                climbing,
                climbingRole,
                false,
                null
        );
        climbingMemberRepository.save(climbingMember);
    }

    public boolean isOwner(Member member, Climbing climbing) {
        return climbingMemberRepository.findByMemberAndClimbing(member, climbing)
                .map(climbingMember -> climbingMember.getRole() == ClimbingRole.OWNER)
                .orElse(false);
    }
}
