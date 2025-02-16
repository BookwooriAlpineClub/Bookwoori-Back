package org.bookwoori.core.domain.messageRoom.facade;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.dto.response.MemberProfileResponseDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.entity.Status;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.messageRoom.dto.request.MessageRoomCreateRequestDto;
import org.bookwoori.core.domain.messageRoom.dto.response.MessageRoomDetailsResponseDto;
import org.bookwoori.core.domain.messageRoom.dto.response.MessageRoomItemDto;
import org.bookwoori.core.domain.messageRoom.dto.response.MessageRoomListResponseDto;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;
import org.bookwoori.core.domain.messageRoom.service.MessageRoomService;
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
    private final MessageRoomService messageRoomService;
    private final MemberService memberService;

    @Transactional
    public MessageRoomDetailsResponseDto getOrCreateMessageRoom(
        MessageRoomCreateRequestDto requestDto) {
        Member sender = memberService.getCurrentMember();
        Member receiver = memberService.getMemberById(requestDto.memberId());

        if (sender.equals(receiver)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Map<Long, MemberProfileResponseDto> members = Map.of(
            sender.getMemberId(), MemberProfileResponseDto.from(sender),
            receiver.getMemberId(), MemberProfileResponseDto.from(receiver)
        );

        MessageRoom messageRoom = messageRoomService.getOrCreateMessageRoom(sender, receiver);
        return MessageRoomDetailsResponseDto.from(messageRoom, receiver.getNickname(), members,
            receiver.getStatus().equals(Status.ACTIVE));
    }

    @Transactional(readOnly = true)
    public MessageRoomListResponseDto getMyMessageRoomList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        //회원이 참여 중인 DM 방 조회
        Member currentMember = memberService.getCurrentMember();
        Page<MessageRoom> messageRooms = messageRoomService.getMessageRoomsByMember(currentMember,
            pageable);
        List<Long> messageRoomIdList = messageRooms.getContent().stream()
            .map(MessageRoom::getMessageRoomId).toList();

        if (messageRoomIdList.isEmpty()) {
            return null;
        }

        //각 DM 방의 마지막 메시지 조회
        Map<Long, RecentDirectMessageResponseDto> messages = chatClient.getRecentMessageFromMessageRoom(
            messageRoomIdList);

        //DTO 구성 및 반환
        List<MessageRoomItemDto> messageRoomItems = messageRooms.getContent().stream()
            .map(messageRoom -> {
                Member partner = messageRoom.getPartner(currentMember);
                RecentDirectMessageResponseDto message = messages.get(
                    messageRoom.getMessageRoomId());
                return MessageRoomItemDto.from(messageRoom.getMessageRoomId(), partner, message);
            }).sorted(Comparator.comparing(
                MessageRoomItemDto::recentMessageTime,
                Comparator.nullsLast(Comparator.reverseOrder())
            )).toList();
        return new MessageRoomListResponseDto(messageRoomItems);
    }

    @Transactional(readOnly = true)
    public Map<Long, MemberProfileResponseDto> getParticipants(Long messageRoomId) {
        MessageRoom messageRoom = messageRoomService.getMessageRoomById(messageRoomId);
        return createProfileMap(messageRoom.getSender(), messageRoom.getReceiver());
    }

    private Map<Long, MemberProfileResponseDto> createProfileMap(Member... members) {
        Map<Long, MemberProfileResponseDto> map = new HashMap<>();
        for (Member member : members) {
            map.put(member.getMemberId(), MemberProfileResponseDto.from(member));
        }
        return map;
    }

}
