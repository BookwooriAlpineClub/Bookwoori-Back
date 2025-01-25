package org.bookwoori.core.domain.record.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.record.repository.RecordRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordRepository {

    private final RecordJpaRepository recordJpaRepository;
}
