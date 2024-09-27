package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.DeliveryDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryService {

    public Delivery saveDelivery(Delivery delivery);

    public Delivery saveDelivery(DeliveryDto delivery);

    public Delivery getDelivery(Long id);

    public DeliveryDto getDeliveryDto(Long id);

    public List<Delivery> getAllDeliveries();

    public void deleteDelivery(Delivery delivery);

    public void deleteDelivery(Long id);

    public Page<Delivery> findAllDeliveries(int page, int pageSize);

    public Page<DeliveryDto> findAllDeliveryDtos(int page, int pageSize);

    public long countDeliveries();


}
