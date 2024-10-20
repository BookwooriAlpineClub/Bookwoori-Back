package org.bookwoori.core.domain.category.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.dto.request.CategoryCreateRequestDto;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryService;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CategoryFacade {

    private final CategoryService categoryService;
    private final ServerService serverService;

    @Transactional
    public void createCategory(CategoryCreateRequestDto requestDto) {
        Server server = serverService.getServerById(requestDto.serverId());
        Category category = requestDto.toEntity(server);
        Category beforeCategory = categoryService.getLastNodeByServer(server);
        category.setBeforeNode(beforeCategory);
        categoryService.saveCategory(category);
    }

}
