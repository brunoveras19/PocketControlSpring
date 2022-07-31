package com.veras.pocketcontrol.services;

import com.veras.pocketcontrol.models.Category;
import com.veras.pocketcontrol.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    public Optional<List<Category>> getAllCategories() {
        return categoryRepository.findAllByUserId(userService.getLoggedUserId());
    }

    public Optional<Category> getCategory(String id) {
        return categoryRepository.findByIdAndUserId(id, userService.getLoggedUserId());
    }

    public Category insertCategory(Category category) {
        category.setUserId(userService.getLoggedUserId());
        Category categoryInserted = categoryRepository.insert(category);
        return categoryInserted;
    }

    public Category updateCategory(Category category) {
        Category categoryToUpdate = categoryRepository.save(category);
        return categoryToUpdate;
    }

    public Category deleteCategory(String id) {
        Category categoryDeleted = categoryRepository.findById(id).get();
        categoryRepository.deleteById(id);
        return categoryDeleted;
    }
}
