package org.bookwoori.core.domain.climbing.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.repository.ClimbingRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClimbingService {
    private final ClimbingRepository climbingRepository;

    public Climbing saveClimbingChannel(Climbing climbing) {return climbingRepository.save(climbing);}

    @Transactional(readOnly = true)
    public Climbing getClimbingById(Long climbingId){
        return climbingRepository.findById(climbingId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLIMBING_NOT_FOUND));
    }
}

