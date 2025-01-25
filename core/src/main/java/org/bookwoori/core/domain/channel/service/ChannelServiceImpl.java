package org.bookwoori.core.domain.channel.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.channel.infrastructure.ChannelEntity;
import org.bookwoori.core.domain.channel.entity.ChannelType;
import org.bookwoori.core.domain.channel.infrastructure.ChannelJpaRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl {

    private final ChannelJpaRepository channelRepository;

    public ChannelEntity saveChannel(ChannelEntity channel) {
        return channelRepository.save(channel);
    }

    public void makeDefaultChannels(CategoryEntity category) {
        ChannelEntity chatChannel = ChannelEntity.builder()
            .category(category)
            .name("일반")
            .channelType(ChannelType.CHAT)
            .build();
        channelRepository.save(chatChannel);
        ChannelEntity voiceChannel = ChannelEntity.builder()
            .category(category)
            .name("일반")
            .channelType(ChannelType.VOICE)
            .build();
        voiceChannel.setBeforeNode(chatChannel);
        channelRepository.save(voiceChannel);
    }

    public void deleteChannel(ChannelEntity channel) {
        channelRepository.delete(channel);
    }

    @Transactional(readOnly = true)
    public ChannelEntity getLastNodeByCategory(CategoryEntity category) {
        return channelRepository.findChannelByCategoryAndNextNodeIsNull(category).orElse(null);
    }

    @Transactional(readOnly = true)
    public ChannelEntity getFirstNodeByCategory(CategoryEntity category) {
        return channelRepository.findChannelByCategoryAndBeforeNodeIsNull(category).orElse(null);
    }

    @Transactional(readOnly = true)
    public ChannelEntity getChannelById(Long channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    @Transactional
    public void detach(ChannelEntity channel) {
        channel.connectBeforeAndNextNodes();
        channelRepository.flush();
    }

    @Transactional
    public void moveChannelsToCategory(CategoryEntity from, CategoryEntity to) {
        ChannelEntity nextChannel = getFirstNodeByCategory(from);
        if (nextChannel != null) {
            nextChannel.setBeforeNode(getLastNodeByCategory(to));
        }
        from.getChannels().forEach(channel -> channel.modifyCategory(to));
    }
}
