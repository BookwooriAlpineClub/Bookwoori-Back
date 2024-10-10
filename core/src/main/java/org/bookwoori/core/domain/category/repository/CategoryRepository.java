package org.bookwoori.core.domain.category.repository;

import org.bookwoori.core.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
