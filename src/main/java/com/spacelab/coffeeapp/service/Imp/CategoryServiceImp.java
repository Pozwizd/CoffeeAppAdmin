package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.CategoryDto;
import com.spacelab.coffeeapp.entity.Category;
import com.spacelab.coffeeapp.mapper.CategoryMapper;
import com.spacelab.coffeeapp.repository.CategoryRepository;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.specification.CategorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public int countCategory() {
        return (int) categoryRepository.count();
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
        log.info("Save category: {}", category);
    }

    @Override
    public void createCategoryFromDto(CategoryDto categoryDto) {
        saveCategory(categoryMapper.toEntity(categoryDto));
        log.info("Save category from dto: {}", categoryDto);
    }

    @Override
    public Optional<Category> getCategory(Long id) {
        log.info("Get category by id: {}", id);
        return categoryRepository.findById(id);
    }

    @Override
    public CategoryDto getCategoryDto(Long id) {
        return categoryMapper.toDto(getCategory(id).get());
    }

    @Override
    public List<Category> getAllCategory() {
        log.info("Get all category");
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryDto> getAllCategoryDto() {
        return categoryMapper.toDtoList(getAllCategory());
    }

    @Override
    public void updateCategory(Long id, Category category) {
        categoryRepository.findById(id).map(category1 -> {
            category1.setName(category.getName());
            category1.setStatus(category.getStatus());
            categoryRepository.save(category1);
            return category1;
        }).orElseThrow(() -> new RuntimeException("Category not found"));
        log.info("Update category: {}", category);
    }

    @Override
    public void updateCategoryFromDto(Long id, CategoryDto category) {
        updateCategory(id, categoryMapper.toEntity(category));
        log.info("Update category from dto: {}", category);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
        log.info("Delete category: {}", category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        log.info("Delete category by id: {}", id);
    }

    @Override
    public Page<Category> findAllCategory(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get categories with pageable: {}", pageable);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Page<Category> findCategoryByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Category> specification = new CategorySpecification(search);
        log.info("Get categories by request: {}", search);
        return categoryRepository.findAll(specification, pageable);
    }

    @Override
    public Page<CategoryDto> getPagedAllCategoryDto(int page, int pageSize, String search) {
        if (search.isEmpty()) {
            return categoryMapper.toDtoListPage(findAllCategory(page, pageSize));
        } else {
            return categoryMapper.toDtoListPage(findCategoryByRequest(page, pageSize, search));
        }
    }


}
