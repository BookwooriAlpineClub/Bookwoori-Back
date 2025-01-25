package org.bookwoori.core.domain.server.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.server.repository.ServerRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ServerRepositoryImpl implements ServerRepository {

    private final ServerJpaRepository serverJpaRepository;
}
