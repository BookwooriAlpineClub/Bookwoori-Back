package org.bookwoori.core.domain.category.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.dto.request.CategoryCreateRequestDto;
import org.bookwoori.core.domain.category.dto.request.CategoryLocateRequestDto;
import org.bookwoori.core.domain.category.dto.request.CategoryUpdateRequestDto;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.category.service.CategoryServiceImpl;
import org.bookwoori.core.domain.channel.service.ChannelServiceImpl;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.server.service.ServerServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CategoryFacade {

    private final ChannelServiceImpl channelService;
    private final CategoryServiceImpl categoryService;
    private final ServerServiceImpl serverService;

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

    @Transactional
    public void locateCategory(Long categoryId, CategoryLocateRequestDto requestDto) {
        Category categoryToMove = categoryService.getCategoryById(categoryId);
        Category beforeCategory = categoryService.getCategoryById(requestDto.beforeCategoryId());

        if (categoryToMove.isDefault()) {
            throw new CustomException(ErrorCode.DEFAULT_CATEGORY_EXCEPTION);
        }

        if (categoryToMove.getCategoryId().equals(beforeCategory.getCategoryId())) {
            return;
        }

        //두 카테고리가 서로 다른 서버에 있을 경우 예외 처리
        if (!categoryToMove.getServer().equals(beforeCategory.getServer())) {
            throw new CustomException(ErrorCode.CATEGORY_LOCATE_EXCEPTION);
        }

        categoryService.detach(categoryToMove);
        categoryService.insert(categoryToMove, beforeCategory);
    }
}
