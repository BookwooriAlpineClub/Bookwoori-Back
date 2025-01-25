package org.bookwoori.core.domain.climbingMember.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.infrastructure.ClimbingMemberEntity;
import org.bookwoori.core.domain.climbingMember.infrastructure.ClimbingMemberJpaRepository;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClimbingMemberServiceImpl {

    private final ClimbingMemberJpaRepository climbingMemberRepository;

    public void saveMember(MemberEntity currentMember, ClimbingEntity climbing,
        ClimbingRole climbingRole) {
        ClimbingMemberEntity climbingMember = new ClimbingMemberEntity(currentMember, climbing,
            climbingRole);
        climbingMemberRepository.save(climbingMember);
    }

    public void removeMember(MemberEntity member, ClimbingEntity climbing) {
        climbingMemberRepository.findByMemberAndClimbing(member, climbing)
            .ifPresent(climbingMemberRepository::delete);
    }

    @Transactional(readOnly = true)
    public boolean isJoined(MemberEntity member, ClimbingEntity climbing) {
        return climbingMemberRepository.existsByMemberAndClimbing(member, climbing);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(MemberEntity member, ClimbingEntity climbing) {
        return climbingMemberRepository.findByMemberAndClimbing(member, climbing)
            .map(climbingMember -> climbingMember.getRole() == ClimbingRole.OWNER)
            .orElse(false);
    }

    @Transactional(readOnly = true)
    public int getMemberCount(ClimbingEntity climbing) {
        return climbingMemberRepository.countByClimbing(climbing);
    }

    @Transactional(readOnly = true)
    public List<ClimbingMemberEntity> getMembersByClimbing(ClimbingEntity climbing) {
        return climbingMemberRepository.findByClimbingWithMember(climbing);
    }

    @Transactional(readOnly = true)
    public ClimbingMemberEntity getMemberInClimbing(MemberEntity member, Long climbingId) {
        return climbingMemberRepository.findByMemberAndClimbing_ClimbingId(member, climbingId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLIMBING_MEMBER_NOT_FOUND));
    }

    public void delegateClimbingRole(Long climbingId, MemberEntity currentMember,
        MemberEntity newOwner) {
        ClimbingMemberEntity currentClimbingMember = getMemberInClimbing(currentMember, climbingId);
        ClimbingMemberEntity newClimbingOwner = getMemberInClimbing(newOwner, climbingId);
        if (currentClimbingMember.getRole() != ClimbingRole.OWNER) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        currentClimbingMember.updateRole(ClimbingRole.MEMBER);
        newClimbingOwner.updateRole(ClimbingRole.OWNER);
    }

    @Transactional(readOnly = true)
    public List<Long> getSharedMemberIds(ClimbingEntity climbing) {
        return climbingMemberRepository.findSharedMemberIdsByClimbing(climbing);
    }

    @Transactional(readOnly = true)
    public ClimbingMemberEntity getClimbingMemberWithMember(Long climbingId, Long memberId) {
        return climbingMemberRepository.findClimbingMemberWithMember(climbingId, memberId);
    }
}
