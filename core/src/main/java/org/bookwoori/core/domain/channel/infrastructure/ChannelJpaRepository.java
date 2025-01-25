package org.bookwoori.core.domain.channel.infrastructure;

import java.util.Optional;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelJpaRepository extends JpaRepository<ChannelEntity, Long> {

    Optional<ChannelEntity> findChannelByCategoryAndNextNodeIsNull(CategoryEntity category);

    Optional<ChannelEntity> findChannelByCategoryAndBeforeNodeIsNull(CategoryEntity category);
}
