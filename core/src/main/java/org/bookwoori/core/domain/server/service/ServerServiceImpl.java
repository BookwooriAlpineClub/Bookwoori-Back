package org.bookwoori.core.domain.server.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.domain.server.repository.ServerRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    public ServerEntity saveServer(ServerEntity server) {
        return serverRepository.save(server);
    }

    public void deleteServer(ServerEntity server) {
        serverRepository.delete(server);
    }

    @Transactional(readOnly = true)
    public ServerEntity getServerById(Long serverId) {
        return serverRepository.findById(serverId)
            .orElseThrow(() -> new CustomException(ErrorCode.SERVER_NOT_FOUND));
    }
}
