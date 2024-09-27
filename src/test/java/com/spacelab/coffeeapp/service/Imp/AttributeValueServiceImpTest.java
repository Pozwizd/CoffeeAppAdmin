package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.mapper.AttributeValueMapper;
import com.spacelab.coffeeapp.repository.AttributeValueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class  AttributeValueServiceImpTest {

    @Mock
    private AttributeValueRepository attributeValueRepository;

    @Mock
    private AttributeValueMapper attributeValueMapper;

    @InjectMocks
    private AttributeValueServiceImp attributeValueService;

    @Test
    public void testSaveAttributeValue() {
        AttributeValue attributeValue = new AttributeValue();
        attributeValueService.saveAttributeValue(attributeValue);
        verify(attributeValueRepository, times(1)).save(attributeValue);
    }

    @Test
    public void testSaveAttributeValueFromDto() {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        AttributeValue attributeValue = new AttributeValue();

        when(attributeValueMapper.toEntity(attributeValueDto)).thenReturn(attributeValue);
        attributeValueService.saveAttributeValueFromDto(attributeValueDto);

        verify(attributeValueRepository, times(1)).save(attributeValue);
        verify(attributeValueMapper, times(1)).toEntity(attributeValueDto);
    }

    @Test
    public void testGetAttributeValue() {
        AttributeValue attributeValue = new AttributeValue();
        when(attributeValueRepository.findById(1L)).thenReturn(Optional.of(attributeValue));

        Optional<AttributeValue> result = attributeValueService.getAttributeValue(1L);

        assertTrue(result.isPresent());
        assertEquals(attributeValue, result.get());
        verify(attributeValueRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAttributeValueNotFound() {
        when(attributeValueRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<AttributeValue> result = attributeValueService.getAttributeValue(1L);

        assertFalse(result.isPresent());
        verify(attributeValueRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAttributeValueDto() {
        AttributeValue attributeValue = new AttributeValue();
        AttributeValueDto attributeValueDto = new AttributeValueDto();

        when(attributeValueRepository.findById(1L)).thenReturn(Optional.of(attributeValue));
        when(attributeValueMapper.toDto(attributeValue)).thenReturn(attributeValueDto);

        AttributeValueDto result = attributeValueService.getAttributeValueDto(1L);

        assertEquals(attributeValueDto, result);
        verify(attributeValueRepository, times(1)).findById(1L);
        verify(attributeValueMapper, times(1)).toDto(attributeValue);
    }

    @Test
    public void testGetAllAttributeValues() {
        List<AttributeValue> attributeValues = Arrays.asList(new AttributeValue(), new AttributeValue());
        when(attributeValueRepository.findAll()).thenReturn(attributeValues);

        List<AttributeValue> result = attributeValueService.getAllAttributeValues();

        assertEquals(attributeValues, result);
        verify(attributeValueRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllAttributeValuesDto() {
        List<AttributeValue> attributeValues = Arrays.asList(new AttributeValue(), new AttributeValue());
        List<AttributeValueDto> attributeValueDtos = Arrays.asList(new AttributeValueDto(), new AttributeValueDto());

        when(attributeValueRepository.findAll()).thenReturn(attributeValues);
        when(attributeValueMapper.toDtoList(attributeValues)).thenReturn(attributeValueDtos);

        List<AttributeValueDto> result = attributeValueService.getAllAttributeValuesDto();

        assertEquals(attributeValueDtos, result);
        verify(attributeValueRepository, times(1)).findAll();
        verify(attributeValueMapper, times(1)).toDtoList(attributeValues);
    }

    @Test
    public void testGetAllAttributeValuesDtoByAttributeId() {
        List<AttributeValue> attributeValues = Arrays.asList(new AttributeValue(), new AttributeValue());
        List<AttributeValueDto> attributeValueDtos = Arrays.asList(new AttributeValueDto(), new AttributeValueDto());

        when(attributeValueRepository.findByAttributeProduct_Id(1L)).thenReturn(attributeValues);
        when(attributeValueMapper.toDtoList(attributeValues)).thenReturn(attributeValueDtos);

        List<AttributeValueDto> result = attributeValueService.getAllAttributeValuesDtoByAttributeId(1L);

        assertEquals(attributeValueDtos, result);
        verify(attributeValueRepository, times(1)).findByAttributeProduct_Id(1L);
        verify(attributeValueMapper, times(1)).toDtoList(attributeValues);
    }

    @Test
    public void testUpdateAttributeValue() {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setName("newName");

        when(attributeValueRepository.findById(1L)).thenReturn(Optional.of(new AttributeValue()));

        attributeValueService.updateAttributeValue(1L, attributeValue);

        verify(attributeValueRepository, times(1)).findById(1L);
        verify(attributeValueRepository, times(1)).save(any(AttributeValue.class));
    }

    @Test
    public void testUpdateAttributeValueNotFound() {
        when(attributeValueRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            attributeValueService.updateAttributeValue(1L, new AttributeValue());
        });

        assertEquals("AttributeValue not found", exception.getMessage());
        verify(attributeValueRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateAttributeValueFromDto() {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        attributeValueDto.setName("newName");

        when(attributeValueRepository.findById(1L)).thenReturn(Optional.of(new AttributeValue()));

        attributeValueService.updateAttributeValueFromDto(1L, attributeValueDto);

        verify(attributeValueRepository, times(1)).findById(1L);
        verify(attributeValueRepository, times(1)).save(any(AttributeValue.class));
    }

    @Test
    public void testUpdateAttributeValueFromDtoNotFound() {
        when(attributeValueRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            attributeValueService.updateAttributeValueFromDto(1L, new AttributeValueDto());
        });

        assertEquals("AttributeValue not found", exception.getMessage());
        verify(attributeValueRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteAttributeValue() {
        AttributeValue attributeValue = new AttributeValue();
        attributeValueService.deleteAttributeValue(attributeValue);
        verify(attributeValueRepository, times(1)).delete(attributeValue);
    }

    @Test
    public void testDeleteAttributeValueById() {
        attributeValueService.deleteAttributeValueById(1L);
        verify(attributeValueRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByAttributeProduct() {
        List<AttributeValue> attributeValues = Arrays.asList(new AttributeValue(), new AttributeValue());
        when(attributeValueRepository.findAttributeValueByAttributeProduct_Id(1L)).thenReturn(attributeValues);

        List<AttributeValue> result = attributeValueService.findByAttributeProduct(1L);

        assertEquals(attributeValues, result);
        verify(attributeValueRepository, times(1)).findAttributeValueByAttributeProduct_Id(1L);
    }
}
