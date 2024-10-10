package org.bookwoori.core.domain.record.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.record.repository.RecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
}
