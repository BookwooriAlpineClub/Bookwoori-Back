package org.bookwoori.chat.domain.channelMessage.repository;

import org.bookwoori.chat.domain.channelMessage.entity.ChannelMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelMessageRepository extends MongoRepository<ChannelMessage, String> {

    Page<ChannelMessage> findByChannelId(Long channelId, Pageable pageable);
}
