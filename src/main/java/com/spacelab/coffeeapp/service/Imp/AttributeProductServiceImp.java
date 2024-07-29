package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.mapper.AttributeProductMapper;
import com.spacelab.coffeeapp.repository.AttributeProductRepository;
import com.spacelab.coffeeapp.service.AttributeProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AttributeProductServiceImp implements AttributeProductService {

    private final AttributeProductRepository attributeProductRepository;
    private final AttributeProductMapper attributeProductMapper;


    @Override
    public void saveAttributeProduct(AttributeProduct attributeProduct) {
        attributeProductRepository.save(attributeProduct);
        log.info("AttributeProduct saved successfully");
    }

    @Override
    public AttributeProduct getAttributeProduct(Long id) {
        log.info("Fetching attributeProduct with id: {}", id);
        return attributeProductRepository.findById(id).get();
    }


    @Override
    public List<AttributeProduct> getAllAttributeProducts() {
        log.info("Fetching all attributeProducts");
        return attributeProductRepository.findAll();
    }

    @Override
    public List<AttributeProduct> findByProduct(Long productId) {
        return attributeProductRepository.findAttributeProductByProduct_Id(productId);
    }

    @Override
    public List<AttributeProductDto> getAllAttributesDtoByProduct(String productId) {
        return attributeProductMapper.toDtoList(findByProduct(Long.valueOf(productId)));
    }


    @Override
    public void updateAttributeProduct(Long id, AttributeProduct attributeProduct) {
        log.info("Updating attributeProduct with id: {}", id);
        attributeProductRepository.findById(id).map(attributeProduct1 -> {
            attributeProduct1.setName(attributeProduct.getName());
            attributeProduct1.setType(attributeProduct.getType());
            attributeProduct1.setProduct(attributeProduct.getProduct());
            attributeProduct1.setAttributeValues(attributeProduct.getAttributeValues());
            attributeProductRepository.save(attributeProduct1);
            return attributeProduct1;
        }).orElseThrow(() -> {
            throw new RuntimeException("AttributeProduct not found");
        });
        log.info("Update attributeProduct: {}", attributeProduct);
    }

    @Override
    public void deleteAttributeProduct(AttributeProduct attributeProduct) {
        attributeProductRepository.delete(attributeProduct);
        log.info("AttributeProduct deleted successfully");
    }

    @Override
    public void deleteAttributeProduct(Long id) {
        attributeProductRepository.deleteById(id);
        log.info("AttributeProduct deleted with id: {}", id);
    }

    @Override
    public Long findMaxId() {
        return attributeProductRepository.findMaxId();
    }
}
