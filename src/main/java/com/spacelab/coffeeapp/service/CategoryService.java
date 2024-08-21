package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.CategoryDto;
import com.spacelab.coffeeapp.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    int countCategory();

    void saveCategory(Category category);

    void createCategoryFromDto(CategoryDto categoryDto);

    Optional<Category> getCategory(Long id);

    CategoryDto getCategoryDto(Long id);

    List<Category> getAllCategory();
    List<CategoryDto> getAllCategoryDto();


    void updateCategory(Long id, Category category);
    void updateCategoryFromDto(Long id, CategoryDto category);


    void deleteCategory(Category category);
    void deleteCategory(Long id);


    Page<Category> findAllCategory(int page, int pageSize);


    Page<Category> findCategoryByRequest(int page, int pageSize, String search);

    Page<CategoryDto> getPagedAllCategoryDto(int page, int pageSize, String search);
}
