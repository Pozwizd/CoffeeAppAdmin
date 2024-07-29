package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.mapper.AttributeValueMapper;
import com.spacelab.coffeeapp.repository.AttributeValueRepository;
import com.spacelab.coffeeapp.service.AttributeValueService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AttributeValueServiceImp implements AttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeValueMapper attributeValueMapper;

    @Override
    public void saveAttributeValue(AttributeValue attributeValue) {
        attributeValueRepository.save(attributeValue);
        log.info("AttributeValue saved successfully");
    }

    @Override
    public void saveAttributeValueFromDto(AttributeValueDto attributeValueDto) {
        AttributeValue attributeValue = attributeValueMapper.toEntity(attributeValueDto);
        attributeValueRepository.save(attributeValue);
        log.info("AttributeValue from dto saved successfully");
    }

    @Override
    public AttributeValue getAttributeValue(Long id) {
        log.info("Fetching attributeValue with id: {}", id);
        return attributeValueRepository.findById(id).get();
    }

    @Override
    public AttributeValueDto getAttributeValueDto(Long id) {
        return attributeValueMapper.toDto(getAttributeValue(id));
    }

    @Override
    public List<AttributeValue> getAllAttributeValues() {
        log.info("Fetching all attributeValues");
        return attributeValueRepository.findAll();
    }

    @Override
    public List<AttributeValueDto> getAllAttributeValuesDto() {
        return attributeValueMapper.toDtoList(getAllAttributeValues());
    }

    @Override
    public List<AttributeValueDto> getAllAttributeValuesDtoByAttributeId(Long attributeId) {
        return attributeValueMapper.toDtoList(attributeValueRepository.findByAttributeProduct_Id(attributeId));
    }

    @Override
    public void updateAttributeValue(Long id, AttributeValue attributeValue) {
        attributeValueRepository.findById(id).map(attributeValue1 -> {
            attributeValue1.setName(attributeValue.getName());
            attributeValue1.setDescription(attributeValue.getDescription());
            attributeValue1.setPrice(attributeValue.getPrice());
            attributeValue1.setPriceWithDiscount(attributeValue.getPriceWithDiscount());
            attributeValue1.setAttributeProduct(attributeValue.getAttributeProduct());
            attributeValueRepository.save(attributeValue1);
            return attributeValue1;
        }).orElseThrow(() -> new RuntimeException("AttributeValue not found"));

        log.info("Update attributeValue: {}", attributeValue);
    }

    @Override
    public void updateAttributeValueFromDto(Long id, AttributeValueDto attributeValueDto) {

        attributeValueRepository.findById(id).map(attributeValue1 -> {
            attributeValue1.setName(attributeValueDto.getName());
            attributeValue1.setDescription(attributeValueDto.getDescription());
            attributeValue1.setPrice(attributeValueDto.getPrice());
            attributeValue1.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
            attributeValueRepository.save(attributeValue1);
            return attributeValue1;
        }).orElseThrow(() -> new RuntimeException("AttributeValue not found"));
    }

    @Override
    public void deleteAttributeValue(AttributeValue attributeValue) {
        attributeValueRepository.delete(attributeValue);
        log.info("AttributeValue deleted successfully");
    }

    @Override
    public void deleteAttributeValueById(Long id) {
        attributeValueRepository.deleteById(id);
        log.info("AttributeValue deleted successfully by id: {}", id);
    }

    @Override
    public List<AttributeValue> findByAttributeProduct(Long id) {
        return attributeValueRepository.findAttributeValueByAttributeProduct_Id(id);
    }
}
