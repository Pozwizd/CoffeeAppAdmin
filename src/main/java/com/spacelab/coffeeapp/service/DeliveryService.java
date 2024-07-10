package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryService {

    public void saveDelivery(Delivery delivery);

    public Delivery getDelivery(Long id);

    public List<Delivery> getAllDeliveries();

    public void updateDelivery(Long id, Delivery delivery);

    public void deleteDelivery(Delivery delivery);

    public void deleteDelivery(Long id);

    public Page findAllDeliveries(int page, int pageSize);

    public Page findDeliveriesByRequest(int page, int pageSize, String search);

    public long countDeliveries();

}
