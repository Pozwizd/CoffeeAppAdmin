package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.CategoryDto;
import com.spacelab.coffeeapp.entity.Category;
import com.spacelab.coffeeapp.mapper.CategoryMapper;
import com.spacelab.coffeeapp.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CategoryServiceImpTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImp categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Electronics");
    }

    @Test
    void testCountCategory() {
        when(categoryRepository.count()).thenReturn(10L);

        int count = categoryService.countCategory();
        assertEquals(10, count);
    }

    @Test
    void testSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.saveCategory(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testCreateCategoryFromDto() {
        when(categoryMapper.toEntity(any(CategoryDto.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.createCategoryFromDto(categoryDto);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.getCategory(1L);
        assertTrue(foundCategory.isPresent());
        assertEquals(category.getId(), foundCategory.get().getId());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategory_NotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Category> foundCategory = categoryService.getCategory(1L);
        assertFalse(foundCategory.isPresent());
    }

    @Test
    void testGetCategoryDto() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto foundCategoryDto = categoryService.getCategoryDto(1L);
        assertNotNull(foundCategoryDto);
        assertEquals(categoryDto.getId(), foundCategoryDto.getId());
    }

//    @Test
//    void testGetAllCategory() {
//        when(categoryRepository.findAll()).thenReturn(List.of(category));
//
//        List<Category> categories = categoryService.getAllCategory();
//        assertEquals(1, categories.size());
//    }

    @Test
    void testGetAllCategoryDto() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDtoList(anyList())).thenReturn(List.of(categoryDto));

        List<CategoryDto> categories = categoryService.getAllCategoryDto();
        assertEquals(1, categories.size());
        assertEquals(categoryDto.getName(), categories.get(0).getName());
    }

    @Test
    void testUpdateCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        categoryService.updateCategory(1L, category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategoryFromDto() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toEntity(any(CategoryDto.class))).thenReturn(category);

        categoryService.updateCategoryFromDto(1L, categoryDto);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

//    @Test
//    void testDeleteCategory() {
//        categoryService.deleteCategory(category);
//        verify(categoryRepository, times(1)).delete(category);
//    }

//    @Test
//    void testDeleteCategoryById() {
//        categoryService.deleteCategory(1L);
//        verify(categoryRepository, times(1)).deleteById(1L);
//    }

//    @Test
//    void testFindAllCategory() {
//        Page<Category> categoryPage = new PageImpl<>(List.of(category));
//        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
//
//        Page<Category> result = categoryService.findAllCategory(0, 10);
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size());
//    }

    @Test
    void testFindCategoryByRequest() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(categoryPage);

        Page<Category> result = categoryService.findCategoryByRequest(0, 10, "Electronics");
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

//    @Test
//    void testGetPagedAllCategoryDto_NoSearch() {
//        Page<Category> categoryPage = new PageImpl<>(List.of(category));
//        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
//        when(categoryMapper.toDtoListPage(any(Page.class))).thenReturn(new PageImpl<>(List.of(categoryDto)));
//
//        Page<CategoryDto> result = categoryService.getPagedAllCategoryDto(0, 10, "");
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size());
//    }

    @Test
    void testGetPagedAllCategoryDto_WithSearch() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(categoryPage);
        when(categoryMapper.toDtoListPage(any(Page.class))).thenReturn(new PageImpl<>(List.of(categoryDto)));

        Page<CategoryDto> result = categoryService.getPagedAllCategoryDto(0, 10, "Electronics");
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}