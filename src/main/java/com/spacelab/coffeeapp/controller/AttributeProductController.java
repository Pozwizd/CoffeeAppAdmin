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
// Проверка изменений
@Controller
@RequiredArgsConstructor
public class AttributeProductController {

    private final AttributeProductService attributeProductService;
    private final CategoryService categoryService;

    // ----------------- Для страницы редактирования продукта --------------

    /*
     * Запрашиваем список атрибутов продукта
     */
    @GetMapping("/attribute/getAll")
    @ResponseBody
    public List<AttributeProductDto> getEntities(HttpSession session) {
        ProductDto product = (ProductDto) session.getAttribute("product");
        return product.getAttributeProducts();
    }


    /*
     * Удаление атрибута
     */
    @DeleteMapping("/attribute/{attributeId}")
    @ResponseBody
    public ResponseEntity<?> deleteAttributeProduct(@PathVariable String attributeId,
                                                    HttpSession session) {


        ProductDto product = (ProductDto) session.getAttribute("product");
        product.getAttributeProducts().removeIf(a -> a.getName().equals(attributeId));
        session.setAttribute("product", product);
        return ResponseEntity.badRequest().build();
    }
    // ----------------- Для страницы редактирования продукта --------------


    // -------------------------- Для страницы Атрибута -----------------------------
    /*
     * Получаем на вход данные с формы продукта
     * Производим валидацию и сохраняем в сессию данные с формы
     * Загружаем страницу Атрибута продукта со значением
     */
    @PostMapping("/product/{id}/attribute/{attributeId}")
    public ModelAndView sendProductIntoSession(@PathVariable String id,
                                               Model model,
                                               @PathVariable String attributeId,
                                               HttpSession session,
                                               @Valid @ModelAttribute ProductDto productDto,
                                               BindingResult bindingResult) {


        ProductDto productFromSession = (ProductDto) session.getAttribute("product");

        if (productFromSession == null) {
            session.setAttribute("error", 1);
            return new ModelAndView("redirect:/product/");
        }

        // Собираем ошибки в новую коллекцию, исключая ошибки для поля 'id'
        List<FieldError> filteredErrors = bindingResult.getFieldErrors().stream()
                .filter(error -> !error.getField().equals("id"))
                .collect(Collectors.toList());


        if (filteredErrors.size() > 0) {
            productDto.setAttributeProducts(productFromSession.getAttributeProducts());
            model.addAttribute("product", productDto);
            model.addAttribute("productId", id);
            model.addAttribute("statusList", Product.Status.values());
            model.addAttribute("listCategories", categoryService.getAllCategoryDto());
            session.setAttribute("product", productDto);
            return new ModelAndView("products/productItem");
        }


        /*
         * Если атрибута нет то создаем новый
         * Если атрибут есть то загружаем страницу с его полями
         */
        productFromSession.setName(productDto.getName());
        productFromSession.setDescription(productDto.getDescription());
        productFromSession.setStatus(productDto.getStatus());
        productFromSession.setCategory(productDto.getCategory());


        session.setAttribute("product", productFromSession);

        return new ModelAndView("redirect:/attribute/" + attributeId);
    }

    @GetMapping("/attribute/{attributeId}")
    public ModelAndView getAttribute(Model model,
                                     @PathVariable String attributeId,
                                     HttpSession session) {


        ProductDto productFromSession = (ProductDto) session.getAttribute("product");

        if (productFromSession == null) {
            session.setAttribute("error", 1);
            return new ModelAndView("redirect:/product/");
        }


        if (attributeId.equals("create")) {
            AttributeProductDto attributeProduct = new AttributeProductDto();
            session.setAttribute("attributeProduct", attributeProduct);
            model.addAttribute("typeAttribute", List.of(AttributeProduct.TypeAttribute.values()));
            model.addAttribute("attributeProductId", attributeId);
            model.addAttribute("attributeProduct", attributeProduct);
            return new ModelAndView("products/AttributeProduct");
        } else {
            AttributeProductDto attributeProduct = productFromSession.getAttributeProducts().stream()
                    .filter(a -> a.getName().equals(attributeId))
                    .findFirst()
                    .orElse(null);
            if (attributeProduct == null) {
                attributeProduct = new AttributeProductDto();
            }

            session.setAttribute("attributeProduct", attributeProduct);

            model.addAttribute("typeAttribute", List.of(AttributeProduct.TypeAttribute.values()));
            model.addAttribute("attributeProductId", attributeProduct.getName());
            model.addAttribute("attributeProduct", attributeProduct);

            return new ModelAndView("products/AttributeProduct");
        }
    }


    @PostMapping("/attribute/{attributeId}/")
    public ModelAndView saveAttribute(Model model,
                                      @PathVariable String attributeId,
                                      HttpSession session,
                                      @Valid @ModelAttribute("attributeProduct") AttributeProductDto attributeProductDto,
                                      BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            ProductDto product = (ProductDto) session.getAttribute("product");
            AttributeProductDto attributeProduct = (AttributeProductDto) session.getAttribute("attributeProduct");

            attributeProductDto.setAttributeValues(attributeProduct.getAttributeValues());

            session.setAttribute("attributeProduct", attributeProduct);

            model.addAttribute("typeAttribute", List.of(AttributeProduct.TypeAttribute.values()));
            model.addAttribute("attributeProduct", attributeProductDto);
            return new ModelAndView("products/AttributeProduct");
        }

        ProductDto product = (ProductDto) session.getAttribute("product");
        AttributeProductDto attributeProductFromSession =
                (AttributeProductDto) session.getAttribute("attributeProduct");

        if (attributeId.equals("create")) {
            attributeProductFromSession = new AttributeProductDto();
            attributeProductFromSession.setName(attributeProductDto.getName());
            attributeProductFromSession.setType(attributeProductDto.getType());

            if (product.getAttributeProducts() == null) {
                product.setAttributeProducts(new ArrayList<>());
            }
            product.getAttributeProducts().add(attributeProductFromSession);
        } else {
            AttributeProductDto finalAttributeProduct = attributeProductFromSession;
            product.getAttributeProducts().stream().filter(a -> a.getName().equals(attributeId))
                    .findFirst().ifPresent(attributeProduct1 -> {
                        attributeProduct1 = finalAttributeProduct;
                    });
            for (AttributeProductDto attributeProduct1 : product.getAttributeProducts()) {
                if (attributeProduct1.getName().equals(attributeId)) {
                    attributeProduct1.setName(attributeProductDto.getName());
                    attributeProduct1.setType(attributeProductDto.getType());
                }
            }
        }
        session.setAttribute("product", product);
        return new ModelAndView("redirect:/product/" + product.getId());
    }


    /*
     * Получаем все значения атрибута продукта по id
     */
    @GetMapping("/attribute/{attributeId}/getAllValue")
    @ResponseBody
    public List<AttributeValueDto> getAllAttributeValue(HttpSession session,
                                                        @PathVariable String attributeId) {

        AttributeProductDto attributeProduct =
                (AttributeProductDto) session.getAttribute("attributeProduct");

        return attributeProduct.getAttributeValues();
    }

    /*
     * Получаем значение атрибута
     */
    @GetMapping("/attributeValue/{attributeValueId}")
    @ResponseBody
    public AttributeValueDto getAttributeValue(@PathVariable String id,
                                               @PathVariable String attributeId,
                                               @PathVariable String attributeValueId,
                                               HttpSession session) {

        AttributeProductDto attributeProduct =
                (AttributeProductDto) session.getAttribute("attributeProduct");

        return Objects.requireNonNull(attributeProduct)
                .getAttributeValues().stream()
                .filter(a -> a.getName().equals(attributeValueId))
                .findFirst()
                .orElse(null);
    }

    /*
     * Обновление значения атрибута
     */
    @PutMapping("/attributeValue/{attributeValueId}")
    @ResponseBody
    public ResponseEntity<?> updateAttributeValue(@PathVariable String attributeValueId,
                                                  HttpSession session,
                                                  @Valid @RequestBody AttributeValueDto attributeValueDto) {

        AttributeProductDto attributeProduct =
                (AttributeProductDto) session.getAttribute("attributeProduct");

        for (AttributeValueDto attributeValue : attributeProduct.getAttributeValues()) {
            if (attributeValue.getName().equals(attributeValueId)) {
                attributeValue.setName(attributeValueDto.getName());
                attributeValue.setPrice(attributeValueDto.getPrice());
                attributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
                attributeValue.setDescription(attributeValueDto.getDescription());

                session.setAttribute("attributeProduct", attributeProduct);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();

    }

    /*
     * Создания значения атрибута
     */
    @PostMapping("/attributeValue/create")
    @ResponseBody
    public ResponseEntity<?> createAttributeValue(HttpSession session,
                                                  @Valid @RequestBody AttributeValueDto attributeValueDto) {
        AttributeProductDto attributeProduct =
                (AttributeProductDto) session.getAttribute("attributeProduct");
        if (attributeProduct.getAttributeValues() == null) {
            attributeProduct.setAttributeValues(new ArrayList<>());
        }
        attributeProduct.getAttributeValues().add(attributeValueDto);

        session.setAttribute("attributeProduct", attributeProduct);

        return ResponseEntity.ok().build();
    }

    /*
     * Удаление значения атрибута из сессии
     */
    @DeleteMapping("/attributeValue/{attributeValueId}")
    @ResponseBody
    public ResponseEntity<?> deleteAttributeValue(@PathVariable String attributeValueId,
                                          HttpSession session) {


        AttributeProductDto attributeProduct =
                (AttributeProductDto) session.getAttribute("attributeProduct");

        for (AttributeValueDto attributeValue : attributeProduct.getAttributeValues()) {
            if (attributeValue.getName().equals(attributeValueId)) {
                attributeProduct.getAttributeValues().remove(attributeValue);
                session.setAttribute("attributeProduct", attributeProduct);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
    // -------------------------- Для страницы Атрибута -----------------------------

    // -------------------------- Для страницы Заказа -----------------------------

    @GetMapping("/attribute/getAttributesByProduct/{productId}")
    @ResponseBody
    public List<AttributeProductDto> getAttributesByProduct(@PathVariable String productId) {

        return attributeProductService.getAllAttributesDtoByProduct(productId);
    }

}
