package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.CategoryDto;
import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.entity.*;
import com.spacelab.coffeeapp.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryMapper {

    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setStatus(Category.Status.valueOf(categoryDto.getStatus()));
        return category;
    }

    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setItemsInCategory(category.getProducts().size());
        categoryDto.setStatus(category.getStatus().toString());

        return categoryDto;
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category l : categories) {
            categoryDtos.add(toDto(l));
        }
        return categoryDtos;
    }

    public Page<CategoryDto> toDtoListPage(Page<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category l : categories.getContent()) {
            categoryDtos.add(toDto(l));
        }
        return new PageImpl<>(categoryDtos, categories.getPageable(), categories.getTotalElements());
    }

    public List<Category> toEntityListPage(List<CategoryDto> categoryDtos) {
        List<Category> locationDtoList = new ArrayList<>();
        for (CategoryDto l : categoryDtos) {
            locationDtoList.add(toEntity(l));
        }
        return locationDtoList;
    }
}
