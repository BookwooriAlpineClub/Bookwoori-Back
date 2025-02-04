package org.bookwoori.core.domain.channel.service;

import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.channel.entity.Channel;

public interface ChannelService {

    Channel save(Channel channel);

    void makeDefaultChannels(Category category);

    void delete(Channel channel);

    Channel getLastNodeByCategory(Category category);

    Channel getFirstNodeByCategory(Category category);

    Channel getChannelById(Long channelId);

    void detach(Channel channel);

    void moveChannelsToCategory(Category from, Category to);
}
