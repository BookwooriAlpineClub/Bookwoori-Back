package org.bookwoori.core.domain.messageRoom.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.messageRoom.repository.MessageRoomRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRoomRepositoryImpl implements MessageRoomRepository {

    private final MessageRoomJpaRepository messageRoomJpaRepository;
}
