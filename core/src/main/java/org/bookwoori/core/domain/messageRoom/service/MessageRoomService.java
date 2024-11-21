package org.bookwoori.core.domain.messageRoom.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;
import org.bookwoori.core.domain.messageRoom.repository.MessageRoomRepository;
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


}
