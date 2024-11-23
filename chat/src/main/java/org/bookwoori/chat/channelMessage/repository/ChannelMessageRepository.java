package org.bookwoori.chat.channelMessage.repository;

import org.bookwoori.chat.channelMessage.domain.ChannelMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelMessageRepository extends MongoRepository<ChannelMessage, String> {

}
