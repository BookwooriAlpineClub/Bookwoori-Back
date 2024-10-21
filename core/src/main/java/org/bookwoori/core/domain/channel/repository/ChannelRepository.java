package org.bookwoori.core.domain.channel.repository;

import java.util.Optional;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findChannelByCategoryAndNextNodeIsNull(Category category);
}
