package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.OrderItemAttributeDto;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import com.spacelab.coffeeapp.repository.OrderItemAttributeRepository;
import com.spacelab.coffeeapp.service.AttributeProductService;
import com.spacelab.coffeeapp.service.AttributeValueService;
import com.spacelab.coffeeapp.service.OrderItemAttributeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemAttributeServiceImp implements OrderItemAttributeService {

    private final OrderItemAttributeRepository orderItemAttributeRepository;
    private final AttributeProductService attributeProductService;
    private final AttributeValueService attributeValueService;


    @Override
    public void deleteOrderItemAttribute(Long id) {
        orderItemAttributeRepository.deleteById(id);
    }

    @Override
    public OrderItemAttribute getOrderItemAttribute(Long id) {
        return orderItemAttributeRepository.findById(id).get();
    }

    @Override
    public Iterable<OrderItemAttribute> getAllOrderItemAttributes() {
        return orderItemAttributeRepository.findAll();
    }

    @Override
    public Iterable<OrderItemAttribute> getAllOrderItemAttributesByOrderItemId(Long orderItemId) {
        return orderItemAttributeRepository.findOrderItemAttributeByOrderItem_Id(orderItemId);
    }

    @Override
    public void deleteAllOrderItemAttributesByOrderItemId(Long orderItemId) {
        orderItemAttributeRepository.deleteByOrderItem_Id(orderItemId);
    }

    @Override
    public OrderItemAttribute saveOrderItemAttribute(OrderItemAttribute orderItemAttribute) {
        orderItemAttribute.setPrice(orderItemAttribute.getAttributeValue().getPriceWithDiscount() != null && orderItemAttribute.getAttributeValue().getPriceWithDiscount() != 0 ?
                orderItemAttribute.getAttributeValue().getPriceWithDiscount() : orderItemAttribute.getAttributeValue().getPrice());
        log.info("OrderItemAttribute saved: {}", orderItemAttribute);
        return orderItemAttributeRepository.save(orderItemAttribute);
    }

    @Override
    public OrderItemAttribute updateOrderItemAttribute(OrderItemAttribute orderItemAttribute) {
        orderItemAttribute.setPrice(orderItemAttribute.getAttributeValue().getPriceWithDiscount() != null && orderItemAttribute.getAttributeValue().getPriceWithDiscount() != 0 ?
                orderItemAttribute.getAttributeValue().getPriceWithDiscount() : orderItemAttribute.getAttributeValue().getPrice());

        return orderItemAttributeRepository.save(orderItemAttribute);
    }

    @Override
    public OrderItemAttribute saveOrderItemAttribute(OrderItemAttributeDto orderItemAttributeDto, OrderItem orderItem) {
        if (orderItemAttributeDto.getId() == null) {
            OrderItemAttribute orderItemAttribute = new OrderItemAttribute();
            setOrderItemAttributeValues(orderItemAttribute, orderItemAttributeDto, orderItem);
            return saveOrderItemAttribute(orderItemAttribute);
        }
        return orderItemAttributeRepository.findById(orderItemAttributeDto.getId()).map(orderItemAttribute -> {
            setOrderItemAttributeValues(orderItemAttribute, orderItemAttributeDto, orderItem);

            return updateOrderItemAttribute(orderItemAttribute);
        }).get();
    }

    private void setOrderItemAttributeValues(OrderItemAttribute orderItemAttribute, OrderItemAttributeDto orderItemAttributeDto, OrderItem orderItem) {
        orderItemAttribute.setAttributeProduct(attributeProductService.getAttributeProduct(orderItemAttributeDto.getProductAttributeId()).get());
        orderItemAttribute.setAttributeValue(attributeValueService.getAttributeValue(orderItemAttributeDto.getAttributeValueId()).get());
        orderItemAttribute.setOrderItem(orderItem);
        // Учитываем скидку если она существует
        orderItemAttribute.setPrice(
                orderItemAttribute
                        .getAttributeValue()
                        .getPriceWithDiscount() != null &&
                        orderItemAttribute
                                .getAttributeValue()
                                .getPriceWithDiscount() != 0 ? orderItemAttribute
                        .getAttributeValue()
                        .getPriceWithDiscount() : orderItemAttribute
                        .getAttributeValue()
                        .getPrice());
    }
}
