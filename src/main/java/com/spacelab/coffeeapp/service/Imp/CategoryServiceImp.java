package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.CategoryDto;
import com.spacelab.coffeeapp.entity.Category;
import com.spacelab.coffeeapp.mapper.CategoryMapper;
import com.spacelab.coffeeapp.repository.CategoryRepository;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import com.spacelab.coffeeapp.specification.CategorySpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductService productService;

    public CategoryServiceImp(CategoryRepository categoryRepository,
                              CategoryMapper categoryMapper,
                              @Lazy ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productService = productService;
    }

    @Override
    public int countCategory() {
        return (int) categoryRepository.count();
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
        log.info("Saved category: {}", category);
    }

    @Override
    public void createCategoryFromDto(CategoryDto categoryDto) {
        saveCategory(categoryMapper.toEntity(categoryDto));
        log.info("Saved category from DTO: {}", categoryDto);
    }

    @Override
    public Optional<Category> getCategory(Long id) {
        log.info("Fetching category by ID: {}", id);
        return categoryRepository.findById(id);
    }

    @Override
    public CategoryDto getCategoryDto(Long id) {
        return getCategory(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategory() {
        log.info("Fetching all categories");
        return categoryRepository.findAll(CategorySpecification.byNotDeleted());
    }

    @Override
    public List<CategoryDto> getAllCategoryDto() {
        return categoryMapper.toDtoList(getAllCategory());
    }

    @Override
    public void updateCategory(Long id, Category category) {
        categoryRepository.findById(id).ifPresentOrElse(existingCategory -> {
            existingCategory.setName(category.getName());
            existingCategory.setStatus(category.getStatus());
            categoryRepository.save(existingCategory);
            log.info("Updated category: {}", existingCategory);
        }, () -> {
            throw new RuntimeException("Category not found");
        });
    }

    @Override
    public void updateCategoryFromDto(Long id, CategoryDto categoryDto) {
        updateCategory(id, categoryMapper.toEntity(categoryDto));
        log.info("Updated category from DTO: {}", categoryDto);
    }

    @Override
    public void deleteCategory(Category category) {
        category.setDeleted(true);
        category.getProducts().forEach(product -> productService.deleteProduct(product.getId()));
        categoryRepository.save(category);
        log.info("Soft deleted category: {}", category);
    }

    @Override
    public Category deleteCategory(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setDeleted(true);
                    category.getProducts().forEach(product -> productService.deleteProduct(product.getId()));
                    categoryRepository.save(category);
                    log.info("Soft deleted category with ID: {}", id);
                    return category;
                }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Page<Category> findAllCategory(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Fetching categories with pagination: {}", pageable);
        return categoryRepository.findAll(CategorySpecification.byNotDeleted(), pageable);
    }

    @Override
    public Page<Category> findCategoryByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Fetching categories by search request: {}", search);
        return categoryRepository.findAll(CategorySpecification.search(search), pageable);
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
