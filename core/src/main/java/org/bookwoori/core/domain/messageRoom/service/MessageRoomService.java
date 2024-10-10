package org.bookwoori.core.domain.messageRoom.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.messageRoom.repository.MessageRoomRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MessageRoomRepository messageRoomRepository;
}
