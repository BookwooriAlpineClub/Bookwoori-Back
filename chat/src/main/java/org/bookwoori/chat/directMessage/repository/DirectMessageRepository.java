package org.bookwoori.chat.directMessage.repository;

import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DirectMessageRepository extends MongoRepository<DirectMessage, String> {

}
