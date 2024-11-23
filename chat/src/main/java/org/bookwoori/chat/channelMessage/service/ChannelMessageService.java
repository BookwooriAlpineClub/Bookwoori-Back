package org.bookwoori.chat.channelMessage.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.channelMessage.repository.ChannelMessageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelMessageService {

    private final ChannelMessageRepository channelMessageRepository;
}
