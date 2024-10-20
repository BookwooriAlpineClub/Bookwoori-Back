package org.bookwoori.core.domain.category.service;


import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.repository.CategoryRepository;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category makeDefaultCategory(Server server) {
        Category category = Category.builder()
            .server(server)
            .name("DEFAULT")
            .isDefault(true)
            .build();
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category getLastNodeByServer(Server server) {
        return categoryRepository.findCategoryByServerAndNextNodeIsNull(server).orElse(null);
    }
}
