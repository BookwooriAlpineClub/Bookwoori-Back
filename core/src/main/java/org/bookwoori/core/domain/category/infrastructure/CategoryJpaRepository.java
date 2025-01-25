package org.bookwoori.core.domain.category.infrastructure;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findCategoryByServerAndNextNodeIsNull(ServerEntity server);

    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.channels WHERE c.server = :server")
    List<CategoryEntity> findCategoriesByServer(ServerEntity server);

    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.channels WHERE c.categoryId = :categoryId")
    Optional<CategoryEntity> findCategoryWithChannelsById(Long categoryId);

    @Query("SELECT c FROM CategoryEntity c WHERE c.server = :server AND c.isDefault = true")
    Optional<CategoryEntity> findDefaultCategoryByServer(ServerEntity server);
}
