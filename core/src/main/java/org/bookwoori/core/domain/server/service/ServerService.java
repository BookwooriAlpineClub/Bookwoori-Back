package org.bookwoori.core.domain.server.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.repository.ServerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    public Server saveServer(Server server) {
        return serverRepository.save(server);
    }
}
