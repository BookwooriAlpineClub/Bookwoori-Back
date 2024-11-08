package org.bookwoori.core.domain.category.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.category.dto.request.CategoryCreateRequestDto;
import org.bookwoori.core.domain.category.dto.request.CategoryUpdateRequestDto;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryService;
import org.bookwoori.core.domain.channel.service.ChannelService;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class CategoryFacade {

    private final ChannelService channelService;
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

    @Transactional
    public void updateCategoryName(Long categoryId, CategoryUpdateRequestDto requestDto) {
        Category category = categoryService.getCategoryById(categoryId);
        category.modifyName(requestDto.name());
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category categoryToDelete = categoryService.getCategoryWithChannels(categoryId);

        //기본 카테고리를 삭제하려는 경우 예외처리
        if (categoryToDelete.isDefault()) {
            throw new CustomException(ErrorCode.DEFAULT_CATEGORY_EXCEPTION);
        }
        Category defaultCategory = categoryService.getDefaultCategoryByServer(
            categoryToDelete.getServer());
        //하위 채널들을 기본 카테고리로 이동
        channelService.moveChannelsToCategory(categoryToDelete, defaultCategory);
        //카테고리 순서 재설정
        categoryService.detach(categoryToDelete);
        categoryService.delete(categoryToDelete);
    }
}
