package org.bookwoori.core.domain.channel.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.category.service.CategoryServiceImpl;
import org.bookwoori.core.domain.channel.dto.request.ChannelCreateRequestDto;
import org.bookwoori.core.domain.channel.dto.request.ChannelModifyRequestDto;
import org.bookwoori.core.domain.channel.infrastructure.ChannelEntity;
import org.bookwoori.core.domain.channel.service.ChannelServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChannelFacade {

    private final ChannelServiceImpl channelService;
    private final CategoryServiceImpl categoryService;

    @Transactional
    public void createChannel(ChannelCreateRequestDto requestDto) {
        CategoryEntity category = categoryService.getCategoryById(requestDto.categoryId());
        ChannelEntity channel = requestDto.toEntity(category);
        ChannelEntity beforeChannel = channelService.getLastNodeByCategory(category);
        channel.setBeforeNode(beforeChannel);
        channelService.saveChannel(channel);
    }

    @Transactional
    public void modifyChannel(Long channelId, ChannelModifyRequestDto requestDto) {
        ChannelEntity channel = channelService.getChannelById(channelId);
        CategoryEntity fromCategory = channel.getCategory();
        CategoryEntity toCategory = categoryService.getCategoryById(requestDto.categoryId());

        if (!fromCategory.getServer().equals(toCategory.getServer())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (!fromCategory.equals(toCategory)) {
            channelService.detach(channel);
            ChannelEntity beforeChannel = channelService.getLastNodeByCategory(toCategory);
            channel.setBeforeNode(beforeChannel);
            channel.modifyCategory(toCategory);
        }

        channel.modifyName(requestDto.name());
    }

    @Transactional
    public void deleteChannel(Long channelId) {
        ChannelEntity channel = channelService.getChannelById(channelId);
        channelService.detach(channel);
        channelService.deleteChannel(channel);
    }
}
