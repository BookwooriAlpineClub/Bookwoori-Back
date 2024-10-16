package org.bookwoori.core.domain.category.service;


import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.repository.CategoryRepository;
import org.bookwoori.core.domain.server.entity.Server;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category makeDefaultCategory(Server server) {
        Category category = Category.builder()
            .server(server)
            .name("DEFAULT")
            .isDefault(true)
            .build();
        return categoryRepository.save(category);
    }
}
