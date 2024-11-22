package org.bookwoori.core.domain.messageRoom.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;
import org.bookwoori.core.domain.messageRoom.repository.MessageRoomRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;

    public MessageRoom saveMessageRoom(MessageRoom messageRoom) {
        return messageRoomRepository.save(messageRoom);
    }

    public MessageRoom getOrCreateMessageRoom(Member sender, Member receiver) {
        return messageRoomRepository.getMessageRoomByMembers(sender, receiver)
            .orElseGet(() -> saveMessageRoom(MessageRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build()));
    }

    public MessageRoom getMessageRoomById(Long messageRoomId) {
        return messageRoomRepository.findById(messageRoomId)
            .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_ROOM_NOT_FOUND));
    }

    public Page<MessageRoom> getMessageRoomsByMember(Member member, Pageable pageable) {
        return messageRoomRepository.getMessageRoomsByMember(member, pageable);
    }
}
