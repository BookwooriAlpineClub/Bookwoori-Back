package org.bookwoori.chat.directMessage.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageItemDto;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageListResponseDto;
import org.bookwoori.chat.directMessage.repository.DirectMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;

    public DirectMessageListResponseDto getDirectMessageHistory(Long roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<DirectMessage> directMessageList =
            directMessageRepository.findByMessageRoomId(roomId, pageable);
        List<DirectMessageItemDto> directMessageDtoList = directMessageList.stream()
            .map(DirectMessageItemDto::from).toList();
        return new DirectMessageListResponseDto(directMessageDtoList);
    }
}
