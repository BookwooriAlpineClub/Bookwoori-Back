package org.bookwoori.core.domain.messageRoom.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.messageRoom.infrastructure.MessageRoomEntity;
import org.bookwoori.core.domain.messageRoom.infrastructure.MessageRoomJpaRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageRoomServiceImpl {

    private final MessageRoomJpaRepository messageRoomRepository;

    public MessageRoomEntity saveMessageRoom(MessageRoomEntity messageRoom) {
        return messageRoomRepository.save(messageRoom);
    }

    public MessageRoomEntity getOrCreateMessageRoom(MemberEntity sender, MemberEntity receiver) {
        return messageRoomRepository.getMessageRoomByMembers(sender, receiver)
            .orElseGet(() -> saveMessageRoom(MessageRoomEntity.builder()
                .sender(sender)
                .receiver(receiver)
                .build()));
    }

    public MessageRoomEntity getMessageRoomById(Long messageRoomId) {
        return messageRoomRepository.findById(messageRoomId)
            .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_ROOM_NOT_FOUND));
    }

    public Page<MessageRoomEntity> getMessageRoomsByMember(MemberEntity member, Pageable pageable) {
        return messageRoomRepository.getMessageRoomsByMember(member, pageable);
    }
}
