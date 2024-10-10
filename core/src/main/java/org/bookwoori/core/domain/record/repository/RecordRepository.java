package org.bookwoori.core.domain.record.repository;

import org.bookwoori.core.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

}
