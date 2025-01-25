package org.bookwoori.core.domain.messageRoom.facade;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.dto.response.MemberProfileResponseDto;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.member.entity.Status;
import org.bookwoori.core.domain.member.service.MemberServiceImpl;
import org.bookwoori.core.domain.messageRoom.dto.request.MessageRoomCreateRequestDto;
import org.bookwoori.core.domain.messageRoom.dto.response.MessageRoomDetailsResponseDto;
import org.bookwoori.core.domain.messageRoom.dto.response.MessageRoomItemDto;
import org.bookwoori.core.domain.messageRoom.dto.response.MessageRoomListResponseDto;
import org.bookwoori.core.domain.messageRoom.infrastructure.MessageRoomEntity;
import org.bookwoori.core.domain.messageRoom.service.MessageRoomServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.feignClient.ChatClient;
import org.bookwoori.core.global.feignClient.dto.RecentDirectMessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MessageRoomFacade {

    private final ChatClient chatClient;
    private final MessageRoomServiceImpl messageRoomService;
    private final MemberServiceImpl memberService;

    @Transactional
    public MessageRoomDetailsResponseDto getOrCreateMessageRoom(
        MessageRoomCreateRequestDto requestDto) {
        MemberEntity sender = memberService.getCurrentMember();
        MemberEntity receiver = memberService.getMemberById(requestDto.memberId());

        if (sender.equals(receiver)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Map<Long, MemberProfileResponseDto> members = Map.of(
            sender.getMemberId(), MemberProfileResponseDto.from(sender),
            receiver.getMemberId(), MemberProfileResponseDto.from(receiver)
        );

        MessageRoomEntity messageRoom = messageRoomService.getOrCreateMessageRoom(sender, receiver);
        return MessageRoomDetailsResponseDto.from(messageRoom, receiver.getNickname(), members,
            receiver.getStatus().equals(Status.ACTIVE));
    }

    @Transactional(readOnly = true)
    public MessageRoomListResponseDto getMyMessageRoomList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        //회원이 참여 중인 DM 방 조회
        MemberEntity currentMember = memberService.getCurrentMember();
        Page<MessageRoomEntity> messageRooms = messageRoomService.getMessageRoomsByMember(
            currentMember,
            pageable);
        List<Long> messageRoomIdList = messageRooms.getContent().stream()
            .map(MessageRoomEntity::getMessageRoomId).toList();

        if (messageRoomIdList.isEmpty()) {
            return null;
        }

        //각 DM 방의 마지막 메시지 조회
        Map<Long, RecentDirectMessageResponseDto> messages = chatClient.getRecentMessageFromMessageRoom(
            messageRoomIdList);

        //DTO 구성 및 반환
        List<MessageRoomItemDto> messageRoomItems = messageRooms.getContent().stream()
            .map(messageRoom -> {
                MemberEntity partner = messageRoom.getPartner(currentMember);
                RecentDirectMessageResponseDto message = messages.get(
                    messageRoom.getMessageRoomId());
                return MessageRoomItemDto.from(messageRoom.getMessageRoomId(), partner, message);
            }).sorted(Comparator.comparing(
                MessageRoomItemDto::recentMessageTime,
                Comparator.nullsLast(Comparator.reverseOrder())
            )).toList();
        return new MessageRoomListResponseDto(messageRoomItems);
    }

}
