package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.*;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import com.spacelab.coffeeapp.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderMapper {

    public OrdersDto toDto(Order order) {
        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setId(order.getId());
        ordersDto.setDateTimeOfCreate(order.getDateTimeOfCreate());
        ordersDto.setDateTimeOfReady(order.getDateTimeOfReady());
        ordersDto.setStatus(order.getStatus());
        ordersDto.setPayment(order.getPayment());
        ordersDto.setTotalAmount(order.getTotalAmount());
        Delivery delivery = order.getDelivery();
        if (delivery != null) {
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
            ordersDto.setDeliveryDto(deliveryDto);
        }
        if (order.getOrderItems() != null) {
            List<OrderItemDto> orderItemsDto = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemDto orderItemDto = new OrderItemDto();
                orderItemDto.setId(orderItem.getId());
                orderItemDto.setQuantity(orderItem.getQuantity());
                orderItemDto.setProductName(orderItem.getProduct().getName());
                orderItemDto.setProductId(orderItem.getProduct().getId());
                orderItemDto.setCategoryId(orderItem.getProduct().getCategory().getId());
                orderItemsDto.add(orderItemDto);
                if (orderItem.getOrderItemAttributes() != null) {
                    List<OrderItemAttributeDto> orderItemAttributeDtos = new ArrayList<>();
                    for (OrderItemAttribute orderItemAttribute : orderItem.getOrderItemAttributes()) {
                        OrderItemAttributeDto orderItemAttributeDto = new OrderItemAttributeDto();
                        orderItemAttributeDto.setId(orderItemAttribute.getId());
                        orderItemAttributeDto.setOrderItemId(orderItemAttribute.getOrderItem().getId());

                        orderItemAttributeDto.setProductAttributeId(orderItemAttribute.getAttributeProduct().getId());

                        orderItemAttributeDto.setAttributeValueId(orderItemAttribute.getAttributeValue().getId());
                        orderItemAttributeDtos.add(orderItemAttributeDto);
                    }
                    if (!orderItemAttributeDtos.isEmpty()) {
                        orderItemDto.setAttributes(orderItemAttributeDtos);
                    }
                }
            }
            ordersDto.setOrderItemsDto(orderItemsDto);
        }

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(order.getCustomer().getId());
        customerDto.setName(order.getCustomer().getName());
        customerDto.setEmail(order.getCustomer().getEmail());
        customerDto.setDateOfBirth(order.getCustomer().getDateOfBirth());
        customerDto.setPhoneNumber(order.getCustomer().getPhoneNumber());
        customerDto.setLanguage(order.getCustomer().getLanguage());
        customerDto.setStatus(order.getCustomer().getStatus().toString());
        ordersDto.setCustomerDto(customerDto);
        return ordersDto;
    }

    public List<OrdersDto> toDto(List<Order> orders) {
        return orders.stream().map(this::toDto).toList();
    }

    public Page<OrdersDto> toDto(Page<Order> orders) {
        return orders.map(this::toDto);
    }
}
