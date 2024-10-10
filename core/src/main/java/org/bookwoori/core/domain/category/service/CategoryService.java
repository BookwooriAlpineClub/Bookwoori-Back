package org.bookwoori.core.domain.category.service;


import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
}
