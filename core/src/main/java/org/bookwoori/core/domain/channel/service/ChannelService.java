package org.bookwoori.core.domain.channel.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.channel.entity.Channel;
import org.bookwoori.core.domain.channel.entity.ChannelType;
import org.bookwoori.core.domain.channel.repository.ChannelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    public void makeDefaultChannels(Category category) {
        Channel chatChannel = Channel.builder()
            .category(category)
            .name("일반")
            .channelType(ChannelType.CHAT)
            .build();
        Channel voiceChannel = Channel.builder()
            .category(category)
            .name("일반")
            .channelType(ChannelType.VOICE)
            .build();
        chatChannel.setNextNode(voiceChannel);
        channelRepository.save(chatChannel);
        channelRepository.save(voiceChannel);
    }
}
