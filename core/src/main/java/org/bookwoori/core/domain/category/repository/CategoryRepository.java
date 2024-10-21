package org.bookwoori.core.domain.category.repository;

import java.util.Optional;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.server.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByServerAndNextNodeIsNull(Server server);
}
