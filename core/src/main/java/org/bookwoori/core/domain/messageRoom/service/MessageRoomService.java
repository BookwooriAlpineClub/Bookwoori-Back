package org.bookwoori.core.domain.messageRoom.service;

import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRoomService {

    MessageRoom save(MessageRoom messageRoom);

    MessageRoom getOrCreateMessageRoom(Member sender, Member receiver);

    Page<MessageRoom> getMessageRoomsByMember(Member member, Pageable pageable);
}
