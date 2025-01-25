package org.bookwoori.core.domain.category.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.category.infrastructure.CategoryJpaRepository;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl {

    private final CategoryJpaRepository categoryRepository;

    public CategoryEntity saveCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public CategoryEntity makeDefaultCategory(ServerEntity server) {
        CategoryEntity category = CategoryEntity.builder()
            .server(server)
            .name("DEFAULT")
            .isDefault(true)
            .build();
        return categoryRepository.save(category);
    }

    @Transactional
    public CategoryEntity getDefaultCategoryByServer(ServerEntity server) {
        return categoryRepository.findDefaultCategoryByServer(server)
            .orElseGet(() -> makeDefaultCategory(server));
    }

    @Transactional(readOnly = true)
    public CategoryEntity getLastNodeByServer(ServerEntity server) {
        return categoryRepository.findCategoryByServerAndNextNodeIsNull(server).orElse(null);
    }

    @Transactional(readOnly = true)
    public CategoryEntity getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public List<CategoryEntity> getCategoriesWithChannels(ServerEntity server) {
        return categoryRepository.findCategoriesByServer(server);
    }

    @Transactional(readOnly = true)
    public CategoryEntity getCategoryWithChannels(Long categoryId) {
        return categoryRepository.findCategoryWithChannelsById(categoryId)
            .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public void detach(CategoryEntity category) {
        category.connectBeforeAndAfterNodes();
        categoryRepository.flush();
    }

    @Transactional
    public void insert(CategoryEntity categoryToInsert, CategoryEntity beforeCategory) {
        CategoryEntity nextCategory = beforeCategory.getNextNode();
        beforeCategory.disconnect();
        categoryRepository.flush();
        categoryToInsert.setBeforeNode(beforeCategory);
        if (nextCategory != null) {
            nextCategory.setBeforeNode(categoryToInsert);
        }
    }

    public void delete(CategoryEntity category) {
        categoryRepository.delete(category);
    }
}
