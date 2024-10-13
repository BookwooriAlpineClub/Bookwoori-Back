package org.bookwoori.core.domain.serverMember.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.serverMember.repository.ServerMemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerMemberService {

    private final ServerMemberRepository serverMemberRepository;
}
