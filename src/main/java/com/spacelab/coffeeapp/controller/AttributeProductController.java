package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.service.AttributeProductService;
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

import java.util.List;
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
        return new ModelAndView("attribute/attributeProduct").addObject("attributeProduct", attributeProductDto);
    }

    @GetMapping("/create")
    public ModelAndView createAttribute(Model model) {
        model.addAttribute("products", productService.getAllProductsDto());
        model.addAttribute("typeAttribute", AttributeProduct.TypeAttribute.values());
        return new ModelAndView("attribute/attributeProduct").addObject("attributeProduct");
    }


    @PostMapping({"/create", "/{attributeId}"})
    @ResponseBody
    public ResponseEntity<?> createAttributeProduct(@Valid @RequestBody AttributeProductDto attributeProductDto, @PathVariable(required = false) String attributeId) {

        Boolean result = attributeProductService.saveAttributeProductFromDto(attributeProductDto);
        System.out.println(result);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping({"/attributeValue/validate"})
    @ResponseBody
    public ResponseEntity<?> createAttributeProductValue(@Valid @RequestBody AttributeValueDto attributeProductDto) {
        return ResponseEntity.ok().build();

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
