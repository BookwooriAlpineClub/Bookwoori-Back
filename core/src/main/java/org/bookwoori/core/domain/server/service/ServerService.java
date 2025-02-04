package org.bookwoori.core.domain.server.service;

import org.bookwoori.core.domain.server.entity.Server;

public interface ServerService {

    Server save(Server server);

    void delete(Server server);

    Server getServerById(Long serverId);
}
