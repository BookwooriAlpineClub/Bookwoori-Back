package org.bookwoori.chat.domain.directMessage.repository;

import java.util.Optional;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DirectMessageRepository extends MongoRepository<DirectMessage, String> {

    Page<DirectMessage> findByMessageRoomId(Long messageRoomId, Pageable pageable);

    Optional<DirectMessage> findTopByMessageRoomIdOrderByCreatedAtDesc(Long messageRoomId);
}
