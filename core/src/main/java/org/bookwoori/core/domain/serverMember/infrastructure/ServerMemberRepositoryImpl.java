package org.bookwoori.core.domain.serverMember.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.serverMember.repository.ServerMemberRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ServerMemberRepositoryImpl implements ServerMemberRepository {

    private final ServerMemberJpaRepository serverMemberJpaRepository;
}
