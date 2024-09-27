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

}
