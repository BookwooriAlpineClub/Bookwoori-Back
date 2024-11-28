package org.bookwoori.chat.domain.directMessage.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.domain.directMessage.dto.response.DirectMessageItemDto;
import org.bookwoori.chat.domain.directMessage.dto.response.DirectMessageListResponseDto;
import org.bookwoori.chat.domain.directMessage.dto.response.RecentDirectMessageResponseDto;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.bookwoori.chat.domain.directMessage.repository.DirectMessageRepository;
import org.bookwoori.chat.global.feignClient.CoreClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final CoreClient coreClient;
    private final DirectMessageRepository directMessageRepository;

    @Transactional(readOnly = true)
    public DirectMessageListResponseDto getDirectMessageHistory(Long roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<DirectMessage> directMessageList =
            directMessageRepository.findByMessageRoomId(roomId, pageable);

        //메시지의 parentId 조회
        List<String> parentIds = directMessageList.stream()
            .map(DirectMessage::getParentId)
            .filter(Objects::nonNull)
            .toList();

        Map<String, String> parentContents = directMessageRepository.findAllById(parentIds).stream()
            .collect(Collectors.toMap(
                DirectMessage::getId,
                message -> message.getContent() != null ? message.getContent() : "삭제된 메시지입니다."
            ));

        List<DirectMessageItemDto> directMessageDtoList = directMessageList.stream()
            .map(directMessage -> {
                if (directMessage.getParentId() != null) {
                    String parentContent = parentContents.getOrDefault(
                        directMessage.getParentId(),
                        "삭제된 메시지입니다."
                    );
                    directMessage.setParentContent(parentContent);
                }
                return DirectMessageItemDto.from(directMessage);
            }).toList();
        return new DirectMessageListResponseDto(directMessageDtoList);
    }

    @Transactional(readOnly = true)
    public Map<Long, RecentDirectMessageResponseDto> getRecentMessageFromMessageRoom(
        List<Long> messageRoomIdList) {
        return messageRoomIdList.stream()
            .collect(Collectors.toMap(
                messageRoomId -> messageRoomId,
                messageRoomId -> directMessageRepository
                    .findTopByMessageRoomIdOrderByCreatedAtDesc(messageRoomId)
                    .map(RecentDirectMessageResponseDto::from)
                    .orElseGet(() -> new RecentDirectMessageResponseDto(null, null))
            ));
    }
}
