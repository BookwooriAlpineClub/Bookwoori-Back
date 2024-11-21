package org.bookwoori.chat.directMessage.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageItemDto;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageListResponseDto;
import org.bookwoori.chat.directMessage.repository.DirectMessageRepository;
import org.bookwoori.chat.global.feignClient.CoreClient;
import org.bookwoori.chat.global.feignClient.dto.MemberProfileResponseDto;
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

        Map<Long, MemberProfileResponseDto> profiles = coreClient.getMembersByMessageRoomId(roomId);
        directMessageList.forEach(directMessage -> {
            MemberProfileResponseDto profile = profiles.get(directMessage.getMemberId());
            directMessage.syncMemberProfile(profile.nickname(), profile.profileImg());
        });

        List<DirectMessageItemDto> directMessageDtoList = directMessageList.stream()
            .map(DirectMessageItemDto::from).toList();
        return new DirectMessageListResponseDto(directMessageDtoList);
    }
}
