package org.bookwoori.chat.channelMessage.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.channelMessage.domain.ChannelMessage;
import org.bookwoori.chat.channelMessage.dto.response.ChannelMessageItemDto;
import org.bookwoori.chat.channelMessage.dto.response.ChannelMessageListResponseDto;
import org.bookwoori.chat.channelMessage.repository.ChannelMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelMessageService {

    private final ChannelMessageRepository channelMessageRepository;

    public ChannelMessageListResponseDto getChannelMessageHistory(Long channelId, int page,
        int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ChannelMessage> channelMessageList =
            channelMessageRepository.findByChannelId(channelId, pageable);
        List<ChannelMessageItemDto> channelMessageDtoList = channelMessageList.stream()
            .map(ChannelMessageItemDto::from).toList();
        return new ChannelMessageListResponseDto(channelMessageDtoList);
    }
}
