package com.spacelab.coffeeapp.service.Imp;


import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.mapper.AttributeProductMapper;
import com.spacelab.coffeeapp.repository.AttributeProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class AttributeProductServiceImpTest {

    @Mock
    private AttributeProductRepository attributeProductRepository;

    @Mock
    private AttributeProductMapper attributeProductMapper;


    @InjectMocks
    private AttributeProductServiceImp attributeProductServiceImp;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void saveAttributeProduct() {
        AttributeProduct attributeProduct = new AttributeProduct();
        attributeProductServiceImp.saveAttributeProduct(attributeProduct);
        verify(attributeProductRepository, times(1)).save(attributeProduct);
    }

    @Test
    void getAttributeProduct() {
        Long id = 1L;
        AttributeProduct attributeProduct = new AttributeProduct();
        when(attributeProductRepository.findById(id)).thenReturn(Optional.of(attributeProduct));
        Optional<AttributeProduct> result = attributeProductServiceImp.getAttributeProduct(id);
        verify(attributeProductRepository, times(1)).findById(id);
        assert(result.isPresent());
    }

    @Test
    void getAttributeProductDto() {
        Long id = 1L;
        AttributeProduct attributeProduct = new AttributeProduct();
        when(attributeProductRepository.findById(id)).thenReturn(Optional.of(attributeProduct));
        AttributeProductDto attributeProductDto = new AttributeProductDto();
        when(attributeProductMapper.toDto(attributeProduct)).thenReturn(attributeProductDto);
        AttributeProductDto result = attributeProductServiceImp.getAttributeProductDto(id);
        verify(attributeProductRepository, times(1)).findById(id);
        verify(attributeProductMapper, times(1)).toDto(attributeProduct);
        assert(result != null);
    }

    @Test
    void getAllAttributeProducts() {
        List<AttributeProduct> attributeProducts = new ArrayList<>();
        when(attributeProductRepository.findAll()).thenReturn(attributeProducts);
        List<AttributeProduct> result = attributeProductServiceImp.getAllAttributeProducts();
        verify(attributeProductRepository, times(1)).findAll();
        assert(result.equals(attributeProducts));
    }

    @Test
    void findByProduct() {
        Long productId = 1L;
        List<AttributeProduct> attributeProducts = new ArrayList<>();
        when(attributeProductRepository.findAll(any(Specification.class))).thenReturn(attributeProducts);
        List<AttributeProduct> result = attributeProductServiceImp.findByProduct(productId);
        verify(attributeProductRepository, times(1)).findAll(any(Specification.class));
        assert(result.equals(attributeProducts));
    }

    @Test
    void getAllAttributesDtoByProduct() {
        String productId = "1";
        List<AttributeProduct> attributeProducts = new ArrayList<>();
        List<AttributeProductDto> attributeProductDtos = new ArrayList<>();
        when(attributeProductRepository.findAll(any(Specification.class))).thenReturn(attributeProducts);
        when(attributeProductMapper.toDtoList(attributeProducts)).thenReturn(attributeProductDtos);
        List<AttributeProductDto> result = attributeProductServiceImp.getAllAttributesDtoByProduct(productId);
        verify(attributeProductRepository, times(1)).findAll(any(Specification.class));
        verify(attributeProductMapper, times(1)).toDtoList(attributeProducts);
        assert(result.equals(attributeProductDtos));
    }

    @Test
    void updateAttributeProduct() {
        Long id = 1L;
        AttributeProduct existingProduct = new AttributeProduct();
        AttributeProduct updatedProduct = new AttributeProduct();
        when(attributeProductRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        attributeProductServiceImp.updateAttributeProduct(id, updatedProduct);
        verify(attributeProductRepository, times(1)).findById(id);
        verify(attributeProductRepository, times(1)).save(existingProduct);
    }

    @Test
    void deleteAttributeProductByObject() {
        AttributeProduct attributeProduct = new AttributeProduct();
        attributeProduct.setId(1L);

        when(attributeProductRepository.findById(anyLong())).thenReturn(Optional.of(attributeProduct));

        boolean result = attributeProductServiceImp.deleteAttributeProduct(attributeProduct);

        verify(attributeProductRepository, times(1)).findById(anyLong());
        verify(attributeProductRepository, times(1)).save(attributeProduct);

        assert(result);
    }

    @Test
    void deleteAttributeProductByObjectEmpty() {
        AttributeProduct attributeProduct = new AttributeProduct();
        attributeProduct.setId(1L);

        when(attributeProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = attributeProductServiceImp.deleteAttributeProduct(attributeProduct);

        verify(attributeProductRepository, times(1)).findById(anyLong());

        assert(!result);
    }


    @Test
    void deleteAttributeProductById() {
        Long id = 1L;
        AttributeProduct attributeProduct = new AttributeProduct();
        when(attributeProductRepository.findById(id)).thenReturn(Optional.of(attributeProduct));
        boolean result = attributeProductServiceImp.deleteAttributeProduct(id);
        verify(attributeProductRepository, times(1)).findById(id);
        verify(attributeProductRepository, times(1)).save(attributeProduct);
        assert(result);
    }

    @Test
    void deleteAttributeProductById_Empty() {
        Long id = 1L;
        when(attributeProductRepository.findById(id)).thenReturn(Optional.empty());
        boolean result = attributeProductServiceImp.deleteAttributeProduct(id);
        verify(attributeProductRepository, times(1)).findById(anyLong());

        assert(!result);
    }

    @Test
    void findMaxId() {
        Long maxId = 1L;
        when(attributeProductRepository.findMaxId()).thenReturn(maxId);
        Long result = attributeProductServiceImp.findMaxId();
        verify(attributeProductRepository, times(1)).findMaxId();
        assert(result.equals(maxId));
    }

    @Test
    void findAllAttributeProductsPage() {
        int page = 0;
        int size = 10;
        Page<AttributeProduct> attributeProductPage = new PageImpl<>(new ArrayList<>());
        when(attributeProductRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(attributeProductPage);
        Page<AttributeProduct> result = attributeProductServiceImp.findAllAttributeProductsPage(page, size);
        verify(attributeProductRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        assert(result.equals(attributeProductPage));
    }


    @Test
    void findAllAttributeProductsPageByRequest() {
        int page = 0;
        int size = 10;
        String search = "search";
        Page<AttributeProduct> attributeProductPage = new PageImpl<>(new ArrayList<>());
        when(attributeProductRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(attributeProductPage);
        Page<AttributeProduct> result = attributeProductServiceImp.findAllAttributeProductsPageByRequest(page, size, search);
        verify(attributeProductRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        assert(result.equals(attributeProductPage));
    }

    @Test
    void findAllAttributeProductsPageByRequestEmpty() {
        int page = 0;
        int size = 10;
        String search = "";
        Page<AttributeProduct> attributeProductPage = new PageImpl<>(new ArrayList<>());
        when(attributeProductRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(attributeProductPage);
        Page<AttributeProduct> result = attributeProductServiceImp.findAllAttributeProductsPageByRequest(page, size, search);
        verify(attributeProductRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        assert(result.equals(attributeProductPage));
    }

    @Test
    void findAttributesDtoByRequest() {
        int page = 0;
        int size = 10;
        String search = "search";
        Page<AttributeProduct> attributeProductPage = new PageImpl<>(new ArrayList<>());
        Page<AttributeProductDto> attributeProductDtoPage = new PageImpl<>(new ArrayList<>());
        when(attributeProductRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(attributeProductPage);
        when(attributeProductMapper.toDtoListPage(attributeProductPage)).thenReturn(attributeProductDtoPage);
        Page<AttributeProductDto> result = attributeProductServiceImp.findAttributesDtoByRequest(page, size, search);
        verify(attributeProductMapper, times(1)).toDtoListPage(attributeProductPage);
        assert(result.equals(attributeProductDtoPage));
    }

    @Test
    void findAttributesDtoByRequestEmpty() {
        int page = 0;
        int size = 10;
        String search = "";
        Page<AttributeProduct> attributeProductPage = new PageImpl<>(new ArrayList<>());
        Page<AttributeProductDto> attributeProductDtoPage = new PageImpl<>(new ArrayList<>());
        when(attributeProductRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(attributeProductPage);
        when(attributeProductMapper.toDtoListPage(attributeProductPage)).thenReturn(attributeProductDtoPage);
        Page<AttributeProductDto> result = attributeProductServiceImp.findAttributesDtoByRequest(page, size, search);
        verify(attributeProductMapper, times(1)).toDtoListPage(attributeProductPage);
        assert(result.equals(attributeProductDtoPage));
    }


    @Test
    void getAttributesByProduct() {
        Long productId = 1L;
        List<AttributeProduct> attributeProducts = new ArrayList<>();
        List<AttributeProductDto> attributeProductDtos = new ArrayList<>();
        when(attributeProductRepository.findAll(any(Specification.class))).thenReturn(attributeProducts);
        when(attributeProductMapper.toDtoList(attributeProducts)).thenReturn(attributeProductDtos);
        List<AttributeProductDto> result = attributeProductServiceImp.getAttributesByProduct(productId);
        verify(attributeProductRepository, times(1)).findAll(any(Specification.class));
        verify(attributeProductMapper, times(1)).toDtoList(attributeProducts);
        assert(result.equals(attributeProductDtos));
    }
}
