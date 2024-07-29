package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.service.AttributeValueService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class AttributeValueController {
    private final AttributeValueService attributeValueService;

    @GetMapping("/attributeValue/{id}")
    @ResponseBody
    public AttributeValueDto getAttributeValue(@PathVariable Long id) {
        return attributeValueService.getAttributeValueDto(id);
    }

    @GetMapping("/attributeValue/")
    @ResponseBody
    public List<AttributeValueDto> getAllAttributeValues() {
        return attributeValueService.getAllAttributeValuesDto();
    }

    @GetMapping("/attributeValue/ByAttributeId/{id}")
    @ResponseBody
    public List<AttributeValueDto> getAttributeValueByAttributeId(@PathVariable Long id) {
        return attributeValueService.getAllAttributeValuesDtoByAttributeId(id);
    }

    @PutMapping("/attributeValue/{id}")
    @ResponseBody
    public ResponseEntity<?> updateAttributeValue(@PathVariable Long id, @RequestBody AttributeValueDto attributeValueDto) {
        attributeValueService.updateAttributeValueFromDto(id, attributeValueDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/attributeValue/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteAttributeValue(@PathVariable Long id) {
        attributeValueService.deleteAttributeValueById(id);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/attributeValue/create")
//    @ResponseBody
//    public ResponseEntity<?> createAttributeValue(@RequestBody AttributeValueDto attributeValueDto) {
//        attributeValueService.saveAttributeValueFromDto(attributeValueDto);
//        return ResponseEntity.ok().build();
//    }


}
