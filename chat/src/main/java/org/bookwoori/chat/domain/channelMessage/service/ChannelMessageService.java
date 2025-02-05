package org.bookwoori.chat.domain.channelMessage.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.domain.channelMessage.dto.response.ChannelMessageItemDto;
import org.bookwoori.chat.domain.channelMessage.dto.response.ChannelMessageListResponseDto;
import org.bookwoori.chat.domain.channelMessage.entity.ChannelMessage;
import org.bookwoori.chat.domain.channelMessage.repository.ChannelMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelMessageService {

    private final ChannelMessageRepository channelMessageRepository;

    public ChannelMessageListResponseDto getChannelMessageList(Long channelId, int page,
        int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ChannelMessage> channelMessageList =
            channelMessageRepository.findByChannelId(channelId, pageable);

        //메시지의 parentId 조회
        List<String> parentIds = channelMessageList.stream()
            .map(ChannelMessage::getParentId)
            .filter(Objects::nonNull)
            .toList();

        Map<String, ChannelMessage> parentMessages = channelMessageRepository.findAllById(parentIds)
            .stream().collect(Collectors.toMap(ChannelMessage::getId, message -> message));

        List<ChannelMessageItemDto> channelMessageDtoList = channelMessageList.stream()
            .map(channelMessage -> {
                if (channelMessage.getParentId() != null) {
                    ChannelMessage parentMessage = parentMessages.get(channelMessage.getParentId());
                    String parentContent =
                        parentMessage != null && parentMessage.getContent() != null
                            ? parentMessage.getContent() : "삭제된 메시지입니다.";
                    Long parentMemberId =
                        parentMessage != null ? parentMessage.getMemberId() : null;
                    channelMessage.setParentContent(parentContent);
                    channelMessage.setParentMemberId(parentMemberId);
                }
                return ChannelMessageItemDto.from(channelMessage);
            }).toList();

        return new ChannelMessageListResponseDto(channelMessageDtoList);
    }
}
