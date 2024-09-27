package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.DeliveryDto;
import com.spacelab.coffeeapp.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class DeliveryMapper {

    public DeliveryDto toDeliveryDto(Delivery delivery) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setId(delivery.getId());
        deliveryDto.setName(delivery.getName());
        deliveryDto.setPhoneNumber(delivery.getPhoneNumber());
        deliveryDto.setCityId(delivery.getCity().getId());
        deliveryDto.setStreet(delivery.getStreet());
        deliveryDto.setBuilding(delivery.getBuilding());
        deliveryDto.setSubDoor(delivery.getSubDoor());
        deliveryDto.setApartment(delivery.getApartment());
        deliveryDto.setDeliveryDate(delivery.getDeliveryDate());
        deliveryDto.setDeliveryTime(delivery.getDeliveryTime());
        deliveryDto.setChangeAmount(delivery.getChangeAmount());
        deliveryDto.setStatus(delivery.getStatus());
        return deliveryDto;
    }

    public Page<DeliveryDto> toDeliveryDtoPage(Page<Delivery> allDeliveries) {
        return allDeliveries.map(this::toDeliveryDto);
    }
}
