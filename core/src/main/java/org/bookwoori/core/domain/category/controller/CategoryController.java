package org.bookwoori.core.domain.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.dto.request.CategoryCreateRequestDto;
import org.bookwoori.core.domain.category.dto.request.CategoryUpdateRequestDto;
import org.bookwoori.core.domain.category.facade.CategoryFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Category")
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryFacade categoryFacade;

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createCategory(
        @Valid @RequestBody CategoryCreateRequestDto requestDto) {
        categoryFacade.createCategory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "카테고리 이름 변경", description = "카테고리 이름을 변경합니다.")
    @PatchMapping("/{categoryId}/name")
    public ResponseEntity<?> updateCategoryName(@PathVariable final Long categoryId,
        @Valid @RequestBody CategoryUpdateRequestDto requestDto) {
        categoryFacade.updateCategoryName(categoryId, requestDto);
        return ResponseEntity.ok().build();
    }
}
