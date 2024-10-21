package org.bookwoori.core.domain.channel.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryService;
import org.bookwoori.core.domain.channel.dto.request.ChannelCreateRequestDto;
import org.bookwoori.core.domain.channel.entity.Channel;
import org.bookwoori.core.domain.channel.service.ChannelService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChannelFacade {

    private final ChannelService channelService;
    private final CategoryService categoryService;

    @Transactional
    public void createChannel(ChannelCreateRequestDto requestDto) {
        Category category = categoryService.getCategoryById(requestDto.categoryId());
        Channel channel = requestDto.toEntity(category);
        Channel beforeChannel = channelService.getLastNodeByCategory(category);
        channel.setBeforeNode(beforeChannel);
        channelService.saveChannel(channel);
    }
}
