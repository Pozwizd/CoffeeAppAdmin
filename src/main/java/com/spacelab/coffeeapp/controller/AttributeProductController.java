package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.CategoryMapper;
import com.spacelab.coffeeapp.mapper.ProductMapper;
import com.spacelab.coffeeapp.service.AttributeProductService;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;




@Controller
@RequiredArgsConstructor
@RequestMapping("/attribute")
public class AttributeProductController {

    private final AttributeProductService attributeProductService;
    private final ProductService productService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "attributeProduct");
        model.addAttribute("title", "Атрибуты продукта");
    }

    @GetMapping({"/", ""})
    public ModelAndView getAttributePage() {

        return new ModelAndView("attribute/attributes");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<AttributeProductDto> getEntities(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "") String search,
                                                         @RequestParam(defaultValue = "5") Integer size) {
        return attributeProductService.findAttributesDtoByRequest(page, size, search);
    }

    @DeleteMapping("/{attributeId}")
    @ResponseBody
    public ResponseEntity<?> deleteAttributeProduct(@PathVariable String attributeId) {

        if (attributeProductService.deleteAttributeProduct(Long.valueOf(attributeId))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{attributeId}")
    public ModelAndView getAttribute(@PathVariable String attributeId, Model model, HttpSession session) {
        AttributeProductDto attributeProductDto = attributeProductService.getAttributeProductDto(Long.valueOf(attributeId));
        model.addAttribute("attributeProduct", attributeProductDto);
        model.addAttribute("products", productService.getAllProductsDto());
        model.addAttribute("typeAttribute", AttributeProduct.TypeAttribute.values());
        session.setAttribute("attributeProduct", attributeProductDto);
        return new ModelAndView("attribute/attributeProduct");
    }

    @PostMapping({"/create", "/{attributeId}"})
    public ModelAndView createAttributeProduct(@Valid @ModelAttribute("attributeProduct") AttributeProductDto attributeProductDto,
                                               BindingResult bindingResult, Model model, @PathVariable String attributeId, HttpSession session) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors().stream()
                    .filter(error -> !Objects.equals(error.getRejectedValue(), ""))
                    .collect(Collectors.toList());
            model.addAttribute("errors", errors);
            return new ModelAndView("attribute/attributeProduct");
        }
        AttributeProductDto attributeProduct = (AttributeProductDto) session.getAttribute("attributeProduct");
        attributeProductDto.setAttributeValues(attributeProduct.getAttributeValues());
        attributeProductService.saveAttributeProductFromDto(attributeProductDto);
        return new ModelAndView("redirect:/attribute");
    }

    @ResponseBody
    @GetMapping("/{attributeId}/values")
    public List<AttributeValueDto> getAttributeValues(@PathVariable String attributeId, HttpSession session) {
        AttributeProductDto attributeProductDto = (AttributeProductDto) session.getAttribute("attributeProduct");
        return attributeProductDto.getAttributeValues();
    }


    @ResponseBody
    @GetMapping("/getAttributesByProduct/{productId}")
    public List<AttributeProductDto> getAttributesByProduct(@PathVariable Long productId) {

        return attributeProductService.getAttributesByProduct(productId);
    }


}
