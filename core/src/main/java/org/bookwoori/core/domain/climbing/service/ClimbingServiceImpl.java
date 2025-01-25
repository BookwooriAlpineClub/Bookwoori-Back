package org.bookwoori.core.domain.climbing.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingJpaRepository;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClimbingServiceImpl {

    private final ClimbingJpaRepository climbingRepository;

    public ClimbingEntity saveClimbingChannel(ClimbingEntity climbing) {
        return climbingRepository.save(climbing);
    }

    @Transactional(readOnly = true)
    public ClimbingEntity getClimbingById(Long climbingId) {
        return climbingRepository.findById(climbingId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLIMBING_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ClimbingEntity> getMyClimbings(MemberEntity member, Long serverId) {
        return climbingRepository.findMyClimbings(member, serverId);
    }

    @Transactional(readOnly = true)
    public List<ClimbingEntity> getReadyClimbings(Long serverId) {
        return climbingRepository.findByServer_ServerIdAndStatus(serverId, ClimbingStatus.READY);
    }

    @Transactional(readOnly = true)
    public List<ClimbingEntity> getRunningClimbs(Long serverId) {
        return climbingRepository.findByServer_ServerIdAndStatus(serverId, ClimbingStatus.RUNNING);
    }

    @Transactional(readOnly = true)
    public List<ClimbingEntity> getEndClimbs(Long serverId) {
        return climbingRepository.findByServer_ServerIdAndStatusIn(
            serverId, List.of(ClimbingStatus.FINISHED, ClimbingStatus.FAILED));
    }

    @Transactional(readOnly = true)
    public List<ClimbingEntity> getAllClimbings() {
        return climbingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void isRunning(Long climbingId) {
        ClimbingEntity climbing = getClimbingById(climbingId);
        if (climbing.getStatus() != ClimbingStatus.RUNNING) {
            throw new CustomException(ErrorCode.CLIMBING_NOT_RUNNING);
        }
    }

    public void deleteClimbing(ClimbingEntity climbing) {
        climbingRepository.delete(climbing);
    }
}

