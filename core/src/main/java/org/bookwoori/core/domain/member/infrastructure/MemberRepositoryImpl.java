package org.bookwoori.core.domain.member.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
}
