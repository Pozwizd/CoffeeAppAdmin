package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    int countCategory();

    void saveCategory(Category category);

    Category getCategory(Long id);
    List<Category> getAllCategory();
    void updateCategory(Long id, Category category);
    void deleteCategory(Category category);
    void deleteCategory(Long id);
    Page<Category> findAllCategory(int page, int pageSize);
    Page<Category> findCategoryByRequest(int page, int pageSize, String search);
}
