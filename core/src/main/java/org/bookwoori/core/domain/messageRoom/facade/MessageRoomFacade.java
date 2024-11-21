package org.bookwoori.core.domain.messageRoom.facade;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.dto.response.MemberProfileResponseDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.messageRoom.dto.request.MessageRoomCreateRequestDto;
import org.bookwoori.core.domain.messageRoom.dto.response.MessageRoomInfoResponseDto;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;
import org.bookwoori.core.domain.messageRoom.service.MessageRoomService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MessageRoomFacade {

    private final MessageRoomService messageRoomService;
    private final MemberService memberService;

    @Transactional
    public MessageRoomInfoResponseDto getOrCreateMessageRoom(
        MessageRoomCreateRequestDto requestDto) {
        Member sender = memberService.getCurrentMember();
        Member receiver = memberService.getMemberById(requestDto.memberId());

        if (sender.equals(receiver)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        MessageRoom messageRoom = messageRoomService.getOrCreateMessageRoom(sender, receiver);
        return MessageRoomInfoResponseDto.from(messageRoom);
    }

    @Transactional(readOnly = true)
    public Map<Long, MemberProfileResponseDto> getMembersProfile(Long messageRoomId) {
        MessageRoom messageRoom = messageRoomService.getMessageRoomById(messageRoomId);
        Member sender = messageRoom.getSender();
        Member receiver = messageRoom.getReceiver();
        Map<Long, MemberProfileResponseDto> profiles = Map.of(
            sender.getMemberId(), MemberProfileResponseDto.from(sender),
            receiver.getMemberId(), MemberProfileResponseDto.from(receiver));
        return profiles;
    }

}
