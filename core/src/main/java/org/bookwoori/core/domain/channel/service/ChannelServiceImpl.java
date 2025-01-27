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
public class ChannelServiceImpl implements ChannelService {

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
        channelRepository.save(chatChannel);
        Channel voiceChannel = Channel.builder()
            .category(category)
            .name("일반")
            .channelType(ChannelType.VOICE)
            .build();
        voiceChannel.setBeforeNode(chatChannel);
        channelRepository.save(voiceChannel);
    }

    public void deleteChannel(Channel channel) {
        channelRepository.delete(channel);
    }

    @Transactional(readOnly = true)
    public Channel getLastNodeByCategory(Category category) {
        return channelRepository.findChannelByCategoryAndNextNodeIsNull(category).orElse(null);
    }

    @Transactional(readOnly = true)
    public Channel getFirstNodeByCategory(Category category) {
        return channelRepository.findChannelByCategoryAndBeforeNodeIsNull(category).orElse(null);
    }

    @Transactional(readOnly = true)
    public Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    @Transactional
    public void detach(Channel channel) {
        channel.connectBeforeAndNextNodes();
        channelRepository.flush();
    }

    @Transactional
    public void moveChannelsToCategory(Category from, Category to) {
        Channel nextChannel = getFirstNodeByCategory(from);
        if (nextChannel != null) {
            nextChannel.setBeforeNode(getLastNodeByCategory(to));
        }
        from.getChannels().forEach(channel -> channel.modifyCategory(to));
    }
}
