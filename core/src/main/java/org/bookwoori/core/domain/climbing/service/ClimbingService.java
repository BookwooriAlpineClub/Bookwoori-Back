package org.bookwoori.core.domain.climbing.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbing.repository.ClimbingRepository;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClimbingService {

    private final ClimbingRepository climbingRepository;

    public Climbing saveClimbingChannel(Climbing climbing) {
        return climbingRepository.save(climbing);
    }

    @Transactional(readOnly = true)
    public Climbing getClimbingById(Long climbingId) {
        return climbingRepository.findById(climbingId)
            .orElseThrow(() -> new CustomException(ErrorCode.CLIMBING_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Climbing> getMyClimbings(Member member, Long serverId) {
        return climbingRepository.findMyClimbings(member, serverId);
    }

    @Transactional(readOnly = true)
    public List<Climbing> getReadyClimbings(Long serverId) {
        return climbingRepository.findReadyClimbings(serverId);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateClimbingStatus() {
        LocalDate today = LocalDate.now();
        List<Climbing> climbingList = climbingRepository.findAll();

        for (Climbing climbing : climbingList) {
            if (climbing.getStartDate().isAfter(today)) {
                climbing.updateStatus(ClimbingStatus.READY);
            } else if (climbing.getStartDate().isEqual(today) || climbing.getEndDate()
                .isAfter(today)) {
                climbing.updateStatus(ClimbingStatus.RUNNING);
            }
//            } else if (climbing.getEndDate().isBefore(today)) {
//                climbing.updateStatus(ClimbingStatus.COMPLETED);
//            }
            climbingRepository.save(climbing);
        }
    }
}

