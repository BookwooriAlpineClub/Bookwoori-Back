package org.bookwoori.core.domain.climbing.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.repository.ClimbingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClimbingService {

    private final ClimbingRepository climbingRepository;

}
