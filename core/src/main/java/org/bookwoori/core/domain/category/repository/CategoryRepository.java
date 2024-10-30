package org.bookwoori.core.domain.category.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.server.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByServerAndNextNodeIsNull(Server server);
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.channels WHERE c.server = :server")
    List<Category> findCategoryByServer(Server server);
}
