package com.spacelab.coffeeapp.controller;


import com.spacelab.coffeeapp.dto.CategoryDto;
import com.spacelab.coffeeapp.entity.Category;
import com.spacelab.coffeeapp.mapper.CategoryMapper;
import com.spacelab.coffeeapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;


    @ModelAttribute public void addAttributes(Model model) {
        model.addAttribute("pageActive", "categories");
        model.addAttribute("title", "Категории");
    }

    @GetMapping({"/", ""}) public ModelAndView index() {
        return new ModelAndView("category/category");
    }

    @GetMapping("/status")
    public @ResponseBody List<Category.Status> getStatus() {

        return List.of(Category.Status.ACTIVE, Category.Status.INACTIVE);
    }


    @GetMapping("/getAll")
    @ResponseBody
    public Page<CategoryDto> getEntities(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "") String search,
                                         @RequestParam(defaultValue = "5") Integer size) {
        return categoryService.getPagedAllCategoryDto(page, size, search);
    }

    @GetMapping("/getAllForOrder")
    @ResponseBody
    public List<CategoryDto> getCategoriesForOrderPage() {
        return categoryService.getAllCategoryDto();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CategoryDto getEntity(@PathVariable Long id) {
        return categoryService.getCategoryDto(id);
    }
    

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<?> createEntity(@RequestBody CategoryDto categoryDto) {

        try {
            categoryService.createCategoryFromDto(categoryDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving category: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateEntity(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto)
    {
        categoryService.updateCategoryFromDto(id, categoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}
