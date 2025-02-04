package org.bookwoori.core.domain.category.service;

import java.util.List;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.server.entity.Server;

public interface CategoryService {

    Category save(Category category);

    Category makeDefaultCategory(Server server);

    Category getDefaultCategoryByServer(Server server);

    Category getLastNodeByServer(Server server);

    Category getCategoryById(Long categoryId);

    List<Category> getCategoriesWithChannels(Server server);

    Category getCategoryWithChannels(Long categoryId);

    void detach(Category category);

    void insert(Category categoryToInsert, Category beforeCategory);

    void delete(Category category);
}
