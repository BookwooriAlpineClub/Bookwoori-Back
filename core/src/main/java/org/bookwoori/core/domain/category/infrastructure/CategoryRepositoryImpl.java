package org.bookwoori.core.domain.category.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
}
