package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.CategoryMapper;
import com.spacelab.coffeeapp.mapper.ProductMapper;
import com.spacelab.coffeeapp.service.AttributeProductService;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "products");
        model.addAttribute("title", "Продукты");
    }

    @GetMapping({"/", ""})
    public ModelAndView getProductPage(HttpSession session) {
        session.removeAttribute("product");
        session.removeAttribute("attributeProduct");
        return new ModelAndView("products/products");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<ProductDto> getProductsDtoWithPagination(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "") String search,
                                                         @RequestParam(defaultValue = "5") Integer size) {
        return productService.findProductsDtoByRequest(page, size, search);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ModelAndView getEditProductPage(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductDto(id));
        List<Product.Status> status = List.of(Product.Status.values());
        model.addAttribute("statusList", status);
        model.addAttribute("listCategories", categoryService.getAllCategoryDto());
        return new ModelAndView("products/productItem");
    }

    @GetMapping("/create")
    public ModelAndView getCreateProductPage(Model model) {
        model.addAttribute("product", new ProductDto());
        List<Product.Status> status = List.of(Product.Status.values());
        model.addAttribute("statusList", status);
        model.addAttribute("listCategories", categoryService.getAllCategoryDto());
        return new ModelAndView("products/productItem");
    }

    @PostMapping("/{id}")
    public ModelAndView updateProduct(@PathVariable Long id,
                                      @Valid @ModelAttribute("product") ProductDto productDto,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDto);
            List<Product.Status> status = List.of(Product.Status.values());
            model.addAttribute("statusList", status);
            model.addAttribute("listCategories", categoryService.getAllCategoryDto());
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return new ModelAndView("products/productItem");
        }

        productService.updateProductFromDto(id, productDto);
        return new ModelAndView("redirect:/product");
    }

    @PostMapping("/create")
    public ModelAndView createProduct(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("products/productItem");
        }
        productService.createProductFromDto(productDto);
        return new ModelAndView("redirect:/product");
    }


    @GetMapping("/getByCategory/{id}")
    @ResponseBody
    public List<ProductDto> getProductsByCategoryId(@PathVariable Long id) {
        return productService.findByCategoryId(id);
    }


}
