package org.bookwoori.core.domain.climbingMember.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.repository.ClimbingMemberRepository;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClimbingMemberService {

    private final ClimbingMemberRepository climbingMemberRepository;

    public void saveMember(Member currentMember, Climbing climbing, ClimbingRole climbingRole) {
        ClimbingMember climbingMember = new ClimbingMember(currentMember, climbing, climbingRole);
        climbingMemberRepository.save(climbingMember);
    }

    public void removeMember(Member member, Climbing climbing) {
        climbingMemberRepository.findByMemberAndClimbing(member, climbing)
            .ifPresent(climbingMemberRepository::delete);
    }

    public boolean isJoined(Member member, Climbing climbing) {
        return climbingMemberRepository.existsByMemberAndClimbing(member, climbing);
    }

    public boolean isOwner(Member member, Climbing climbing) {
        return climbingMemberRepository.findByMemberAndClimbing(member, climbing)
            .map(climbingMember -> climbingMember.getRole() == ClimbingRole.OWNER)
            .orElse(false);
    }
}
