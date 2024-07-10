package com.spacelab.coffeeapp.controller.admin;


import com.spacelab.coffeeapp.dto.CategoryDto;
import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.entity.Category;
import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.mapper.CategoryMapper;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.CityService;
import com.spacelab.coffeeapp.service.UserService;
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
    private final CityService cityService;
    private final UserService userService;
    private final CategoryMapper categoryMapper;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "categories");
        model.addAttribute("title", "Категории");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
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
        if (search.isEmpty()) {
            return categoryMapper.toDtoListPage(categoryService.findAllCategory(page, size));
        } else {
            return categoryMapper.toDtoListPage(categoryService.findCategoryByRequest(page, size, search));
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CategoryDto getEntity(@PathVariable Long id) {
        return categoryMapper.toDto(categoryService.getCategory(id));
    }

    @PostMapping({"/", ""})
    @ResponseBody
    public ResponseEntity<?> createEntity(@RequestBody CategoryDto categoryDto) {
        categoryService.saveCategory(categoryMapper.toEntity(categoryDto));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateEntity(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto)
    {
        categoryService.updateCategory(id, categoryMapper.toEntity(categoryDto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}
