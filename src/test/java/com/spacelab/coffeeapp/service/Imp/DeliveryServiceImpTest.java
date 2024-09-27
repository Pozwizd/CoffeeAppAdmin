package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.DeliveryDto;
import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.mapper.DeliveryMapper;
import com.spacelab.coffeeapp.repository.DeliveryRepository;
import com.spacelab.coffeeapp.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DeliveryServiceImpTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @Mock
    private CityService cityService;

    @InjectMocks
    private DeliveryServiceImp deliveryService;

    private Delivery delivery;
    private DeliveryDto deliveryDto;
    private City city;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        delivery = new Delivery();
        delivery.setId(1L);
        delivery.setName("Test Delivery");

        deliveryDto = new DeliveryDto();
        deliveryDto.setId(1L);
        deliveryDto.setName("Test Delivery");

        city = new City();
        city.setId(1L);
    }

    @Test
    void testSaveDelivery_WithId() {
        when(deliveryRepository.findById(anyLong())).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);
        when(cityService.getCity(anyLong())).thenReturn(city);

        Delivery savedDelivery = deliveryService.saveDelivery(deliveryDto);
        assertNotNull(savedDelivery);
        verify(deliveryRepository, times(1)).findById(deliveryDto.getId());
        verify(deliveryRepository, times(1)).save(delivery);
    }

    @Test
    void testSaveDelivery_WithoutId() {
        deliveryDto.setId(null);
        when(cityService.getCity(anyLong())).thenReturn(city);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        Delivery savedDelivery = deliveryService.saveDelivery(deliveryDto);
        assertNotNull(savedDelivery);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void testSaveDelivery_Entity() {
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        Delivery savedDelivery = deliveryService.saveDelivery(delivery);
        assertNotNull(savedDelivery);
        verify(deliveryRepository, times(1)).save(delivery);
    }

    @Test
    void testGetDelivery() {
        when(deliveryRepository.getReferenceById(anyLong())).thenReturn(delivery);

        Delivery foundDelivery = deliveryService.getDelivery(1L);
        assertNotNull(foundDelivery);
        assertEquals(delivery.getId(), foundDelivery.getId());
        verify(deliveryRepository, times(1)).getReferenceById(1L);
    }

    @Test
    void testGetDeliveryDto() {
        when(deliveryRepository.getReferenceById(anyLong())).thenReturn(delivery);
        when(deliveryMapper.toDeliveryDto(any(Delivery.class))).thenReturn(deliveryDto);

        DeliveryDto foundDeliveryDto = deliveryService.getDeliveryDto(1L);
        assertNotNull(foundDeliveryDto);
        verify(deliveryRepository, times(1)).getReferenceById(1L);
        verify(deliveryMapper, times(1)).toDeliveryDto(any(Delivery.class));
    }

    @Test
    void testGetAllDeliveries() {
        when(deliveryRepository.findAll()).thenReturn(List.of(delivery));

        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        assertEquals(1, deliveries.size());
    }

    @Test
    void testDeleteDelivery_Entity() {
        deliveryService.deleteDelivery(delivery);
        verify(deliveryRepository, times(1)).delete(delivery);
    }

    @Test
    void testDeleteDelivery_ById() {
        deliveryService.deleteDelivery(1L);
        verify(deliveryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllDeliveries() {
        Page<Delivery> deliveryPage = new PageImpl<>(List.of(delivery));
        when(deliveryRepository.findAll(any(Pageable.class))).thenReturn(deliveryPage);

        Page<Delivery> result = deliveryService.findAllDeliveries(0, 10);
        assertNotNull(result);
    }

    @Test
    void testFindAllDeliveryDtos() {
        Page<Delivery> deliveryPage = new PageImpl<>(List.of(delivery));
        Page<DeliveryDto> deliveryDtoPage = new PageImpl<>(List.of(deliveryDto));
        when(deliveryRepository.findAll(any(Pageable.class))).thenReturn(deliveryPage);
        when(deliveryMapper.toDeliveryDtoPage(any(Page.class))).thenReturn(deliveryDtoPage);

        Page<DeliveryDto> result = deliveryService.findAllDeliveryDtos(0, 10);
        assertNotNull(result);
    }

    @Test
    void testCountDeliveries() {
        when(deliveryRepository.count()).thenReturn(1L);
        long count = deliveryService.countDeliveries();
        assertEquals(1, count);
    }
}
