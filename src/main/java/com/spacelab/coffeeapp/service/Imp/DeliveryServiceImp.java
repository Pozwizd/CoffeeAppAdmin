package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.repository.DeliveryRepository;
import com.spacelab.coffeeapp.service.DeliveryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class DeliveryServiceImp implements DeliveryService {

    private final DeliveryRepository deliveryRepository;



    @Override
    public void saveDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
        log.info("Save delivery: {}", delivery);
    }

    @Override
    public Delivery getDelivery(Long id) {
        log.info("Get delivery by id: {}", id);
        return deliveryRepository.getReferenceById(id);
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    public void updateDelivery(Long id, Delivery delivery) {
        deliveryRepository.findById(id).map(delivery1 -> {
            delivery1.setName(delivery.getName());
            delivery1.setPhoneNumber(delivery.getPhoneNumber());
            delivery1.setStreet(delivery.getStreet());
            delivery1.setBuilding(delivery.getBuilding());
            delivery1.setApartment(delivery.getApartment());
            delivery1.setDeliveryTime(delivery.getDeliveryTime());
            delivery1.setChangeAmount(delivery.getChangeAmount());
            delivery1.setStatus(delivery.getStatus());
            delivery1.setCity(delivery.getCity());
            deliveryRepository.save(delivery);
            log.info("Update delivery: {}", delivery);
            return delivery;
        }).orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    @Override
    public void deleteDelivery(Delivery delivery) {
        deliveryRepository.delete(delivery);
        log.info("Delete delivery: {}", delivery);
    }

    @Override
    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
        log.info("Delete delivery by id: {}", id);
    }

    @Override
    public Page findAllDeliveries(int page, int pageSize) {
        return deliveryRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page findDeliveriesByRequest(int page, int pageSize, String search) {
        return null;
    }

    @Override
    public long countDeliveries() {
        return deliveryRepository.count();
    }
}
