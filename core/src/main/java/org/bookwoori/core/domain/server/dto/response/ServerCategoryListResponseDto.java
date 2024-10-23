package org.bookwoori.core.domain.server.dto.response;

import java.util.List;
import org.bookwoori.core.domain.category.dto.response.CategoryResponseDto;

public record ServerCategoryListResponseDto(
    List<CategoryResponseDto> categories
) {
}
