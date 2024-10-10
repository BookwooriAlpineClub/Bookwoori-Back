package org.bookwoori.core.domain.channel.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.channel.repository.ChannelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private ChannelRepository channelRepository;
}
