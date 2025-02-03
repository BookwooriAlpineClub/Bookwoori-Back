package org.bookwoori.core.domain.channel.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryServiceImpl;
import org.bookwoori.core.domain.channel.dto.request.ChannelCreateRequestDto;
import org.bookwoori.core.domain.channel.dto.request.ChannelModifyRequestDto;
import org.bookwoori.core.domain.channel.entity.Channel;
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

    /* TODO @crHwang0822 리팩토링 */
    @Transactional
    public void createChannel(ChannelCreateRequestDto requestDto) {
        Category category = categoryService.getCategoryById(requestDto.categoryId());
        Channel channel = requestDto.toEntity(category);
        Channel beforeChannel = channelService.getLastNodeByCategory(category);
        channel.setBeforeNode(beforeChannel);
        channelService.save(channel);
    }

    /* TODO @crHwang0822 리팩토링 */
    @Transactional
    public void modifyChannel(Long channelId, ChannelModifyRequestDto requestDto) {
        Channel channel = channelService.getChannelById(channelId);
        Category fromCategory = channel.getCategory();
        Category toCategory = categoryService.getCategoryById(requestDto.categoryId());

        if (!fromCategory.getServer().equals(toCategory.getServer())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (!fromCategory.equals(toCategory)) {
            channelService.detach(channel);
            Channel beforeChannel = channelService.getLastNodeByCategory(toCategory);
            channel.setBeforeNode(beforeChannel);
            channel.modifyCategory(toCategory);
        }

        channel.modifyName(requestDto.name());
    }

    /* TODO @crHwang0822 리팩토링 */
    @Transactional
    public void deleteChannel(Long channelId) {
        Channel channel = channelService.getChannelById(channelId);
        channelService.detach(channel);
        channelService.delete(channel);
    }
}
