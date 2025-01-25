package org.bookwoori.core.domain.channel.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.channel.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChannelRepositoryImpl implements ChannelRepository {

    private final ChannelJpaRepository channelJpaRepository;
}
