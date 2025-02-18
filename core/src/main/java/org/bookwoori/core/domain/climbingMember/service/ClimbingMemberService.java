package org.bookwoori.core.domain.climbingMember.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingMember.repository.ClimbingMemberRepository;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    @Transactional(readOnly = true)
    public boolean isJoined(Member member, Climbing climbing) {
        return climbingMemberRepository.existsByMemberAndClimbing(member, climbing);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Member member, Climbing climbing) {
        return climbingMemberRepository.findByMemberAndClimbing(member, climbing)
            .map(climbingMember -> climbingMember.getRole() == ClimbingRole.OWNER)
            .orElse(false);
    }

    @Transactional(readOnly = true)
    public int getMemberCount(Climbing climbing) {
        return climbingMemberRepository.countByClimbing(climbing);
    }

    @Transactional(readOnly = true)
    public List<ClimbingMember> getMembersByClimbing(Climbing climbing) {
        return climbingMemberRepository.findByClimbingWithMember(climbing);
    }

    @Transactional(readOnly = true)
    public ClimbingMember getMemberInClimbing(Member member, Long climbingId) {
        return climbingMemberRepository.findByMemberAndClimbing_ClimbingId(member, climbingId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLIMBING_MEMBER_NOT_FOUND));
    }

    public void delegateClimbingRole(Long climbingId, Member currentMember,
        Member newOwner) {
        ClimbingMember currentClimbingMember = getMemberInClimbing(currentMember, climbingId);
        ClimbingMember newClimbingOwner = getMemberInClimbing(newOwner, climbingId);
        if (currentClimbingMember.getRole() != ClimbingRole.OWNER) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        currentClimbingMember.updateRole(ClimbingRole.MEMBER);
        newClimbingOwner.updateRole(ClimbingRole.OWNER);
    }

    @Transactional(readOnly = true)
    public List<Long> getSharedMemberIds(Climbing climbing) {
        return climbingMemberRepository.findSharedMemberIdsByClimbing(climbing);
    }

    @Transactional(readOnly = true)
    public ClimbingMember getClimbingMemberWithMember(Long climbingId, Long memberId) {
        return climbingMemberRepository.findClimbingMemberWithMember(climbingId, memberId);
    }

    @Transactional(readOnly = true)
    public List<Long> getSharedClimbingMemberIds(Climbing climbing) {
        return climbingMemberRepository.findSharedClimbingMemberIdsByClimbing(climbing);
    }

    @Transactional(readOnly = true)
    public ClimbingMember getClimbingMemberById(Long climbingMemberId){
        return climbingMemberRepository.findById(climbingMemberId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLIMBING_MEMBER_NOT_FOUND));
    }


}
