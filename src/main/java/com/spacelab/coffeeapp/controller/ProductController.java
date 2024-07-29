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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

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

    @GetMapping("/getByCategory/{categoryId}")
    @ResponseBody
    public List<ProductDto> getProductsByCategory( @PathVariable String categoryId) {
        List<ProductDto> products = productMapper.toDtoList(productService.getProductsByCategory(Long.valueOf(categoryId)));
        return products;
    }



    @GetMapping("/{id}")
    public ModelAndView getEntity(@PathVariable String id, HttpSession session,  Model model) {
        ProductDto product = (ProductDto) session.getAttribute("product");

        session.removeAttribute("attributeProduct");
        model.addAttribute("statusList", Product.Status.values());
        model.addAttribute("listCategories", categoryService.getAllCategory()
                .stream().map(categoryMapper::toDto).toList());

        if (product == null){
            if (id.equals("create")) {
                ProductDto productDto = new ProductDto();
                model.addAttribute("product",
                        productDto);
                model.addAttribute("productId", "create");
                session.setAttribute("product", productDto);
            } else {
                ProductDto productDto = productService.getProductDto(Long.valueOf(id));
                session.setAttribute("product", productDto );
                model.addAttribute("productId", productDto.getId());
                model.addAttribute("product",
                        productDto);
            }
        } else {
            session.setAttribute("product", product);
            model.addAttribute("product", product);
        }
        return new ModelAndView("products/productItem");

    }

    @PostMapping("/{id}")
    public ModelAndView updateEntity(@PathVariable String id,
                                     HttpSession session,
                                     @Valid @ModelAttribute("product") ProductDto productDto,
                                     BindingResult bindingResult,
                                     Model model) {

        ProductDto productFromSession = (ProductDto) session.getAttribute("product");


        // Собираем ошибки в новую коллекцию, исключая ошибки для поля 'id'
        List<FieldError> filteredErrors = bindingResult.getFieldErrors().stream()
                .filter(error -> !error.getField().equals("id"))
                .collect(Collectors.toList());


        if (filteredErrors.size() > 0) {
            productDto.setAttributeProducts(productFromSession.getAttributeProducts());
            model.addAttribute("product", productDto);
            model.addAttribute("productId", id);
            model.addAttribute("statusList", Product.Status.values());
            model.addAttribute("listCategories", categoryService.getAllCategory()
                    .stream().map(categoryMapper::toDto).toList());
            session.setAttribute("product", productDto);
            return new ModelAndView("products/productItem");
        }

        if (id.equals("create") && id == null) {
            productService.createProductFromDto(productDto);
        } else {
            productFromSession.setName(productDto.getName());
            productFromSession.setDescription(productDto.getDescription());
            productFromSession.setStatus(productDto.getStatus());
            productFromSession.setCategory(productDto.getCategory());

            productService.updateProductFromDto(Long.valueOf(id),productFromSession);
        }
        return new ModelAndView("redirect:/product");
    }



    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
