package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.service.AttributeValueService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/attributeValue")
@AllArgsConstructor
public class AttributeValueController {
    private final AttributeValueService attributeValueService;


    @GetMapping({"/", ""})
    @ResponseBody
    public List<AttributeValueDto> getAllAttributeValues() {
        return attributeValueService.getAllAttributeValuesDto();
    }


    @GetMapping("/getAll")
    @ResponseBody
    public List<AttributeValueDto> getAttributeValueByAttributeId(HttpSession session) {
        AttributeProductDto attributeProduct = (AttributeProductDto) session.getAttribute("attributeProduct");
        return attributeProduct.getAttributeValues();
    }

    @GetMapping("/getAllForOrder")
    @ResponseBody
    public List<AttributeValueDto> getAttributeValue(HttpSession session) {
        return attributeValueService.getAllAttributeValuesDto();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteAttributeValue(@PathVariable Long id) {
        attributeValueService.deleteAttributeValueById(id);
        return ResponseEntity.ok().build();
    }

    /*
     * Получаем значение атрибута
     */
    @GetMapping("/{attributeValueId}")
    @ResponseBody
    public AttributeValueDto getAttributeValue(@PathVariable String attributeValueId,
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
    @PutMapping("/{attributeValueId}")
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

    @PostMapping("/create")
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

    @DeleteMapping("/{attributeValueId}")
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

}
