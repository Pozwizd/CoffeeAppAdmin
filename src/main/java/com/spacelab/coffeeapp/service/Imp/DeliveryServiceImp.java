package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.DeliveryDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.mapper.DeliveryMapper;
import com.spacelab.coffeeapp.repository.DeliveryRepository;
import com.spacelab.coffeeapp.service.CityService;
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
    private final DeliveryMapper deliveryMapper;
    private final CityService cityService;



    @Override
    public Delivery saveDelivery(DeliveryDto deliveryDto) {
        if (deliveryDto.getId() != null) {
            return deliveryRepository.findById(deliveryDto.getId())
                    .map(existingDelivery -> updateDelivery(existingDelivery, deliveryDto))
                    .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + deliveryDto.getId()));
        } else {
            Delivery newDelivery = createDelivery(deliveryDto);
            return saveDelivery(newDelivery);
        }
    }

    private Delivery updateDelivery(Delivery existingDelivery, DeliveryDto deliveryDto) {
        applyDeliveryDtoToDelivery(existingDelivery, deliveryDto);
        log.info("Updated delivery: {}", existingDelivery);
        return saveDelivery(existingDelivery);
    }

    private Delivery createDelivery(DeliveryDto deliveryDto) {
        Delivery newDelivery = new Delivery();
        applyDeliveryDtoToDelivery(newDelivery, deliveryDto);
        log.info("Created new delivery: {}", newDelivery);
        return newDelivery;
    }

    private void applyDeliveryDtoToDelivery(Delivery delivery, DeliveryDto deliveryDto) {
        delivery.setName(deliveryDto.getName());
        delivery.setPhoneNumber(deliveryDto.getPhoneNumber());
        delivery.setStreet(deliveryDto.getStreet());
        delivery.setBuilding(deliveryDto.getBuilding());
        delivery.setApartment(deliveryDto.getApartment());
        delivery.setDeliveryTime(deliveryDto.getDeliveryTime());
        delivery.setChangeAmount(deliveryDto.getChangeAmount());
        delivery.setStatus(deliveryDto.getStatus());
        delivery.setCity(cityService.getCity(deliveryDto.getCityId()));
    }

    @Override
    public Delivery saveDelivery(Delivery delivery) {
        log.info("Save delivery: {}", delivery);
        return deliveryRepository.save(delivery);
    }


    @Override
    public Delivery getDelivery(Long id) {
        log.info("Get delivery by id: {}", id);
        return deliveryRepository.getReferenceById(id);
    }

    @Override
    public DeliveryDto getDeliveryDto(Long id) {
        return deliveryMapper.toDeliveryDto(getDelivery(id));
    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
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
    public Page<Delivery> findAllDeliveries(int page, int pageSize) {
        return deliveryRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<DeliveryDto> findAllDeliveryDtos(int page, int pageSize) {
        return deliveryMapper.toDeliveryDtoPage(findAllDeliveries(page, pageSize));
    }

    @Override
    public long countDeliveries() {
        return deliveryRepository.count();
    }

}
