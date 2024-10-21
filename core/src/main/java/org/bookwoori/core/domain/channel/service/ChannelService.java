package org.bookwoori.core.domain.channel.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.channel.entity.Channel;
import org.bookwoori.core.domain.channel.entity.ChannelType;
import org.bookwoori.core.domain.channel.repository.ChannelRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    public Channel saveChannel(Channel channel) {
        return channelRepository.save(channel);
    }

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

    @Transactional(readOnly = true)
    public Channel getLastNodeByCategory(Category category) {
        return channelRepository.findChannelByCategoryAndNextNodeIsNull(category).orElse(null);
    }

    @Transactional(readOnly = true)
    public Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
