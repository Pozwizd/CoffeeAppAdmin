package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.OrderItemDto;
import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.mapper.OrderMapper;
import com.spacelab.coffeeapp.repository.OrderRepository;
import com.spacelab.coffeeapp.service.DeliveryService;
import com.spacelab.coffeeapp.service.OrderItemService;
import com.spacelab.coffeeapp.service.OrderService;
import com.spacelab.coffeeapp.specification.OrderSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImp implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final DeliveryService deliveryService;
    private final OrderItemService orderItemService;
    @Override
    public void saveOrder(Order order) {
        log.info("Save order: {}", order);
        orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        log.info("Get order by id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public OrdersDto getOrderDto(Long id) {
        log.info("Get orderDto by id: {}", id);
        return orderMapper.toDto(getOrder(id));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void updateOrder(Long id, Order order) {
        orderRepository.findById(id).map(order1 -> {
            order1.setDateTimeOfCreate(order.getDateTimeOfCreate());
            order1.setDateTimeOfReady(order.getDateTimeOfReady());
            order1.setDelivery(order.getDelivery());
            order1.setOrderItems(order.getOrderItems());
            order1.setPayment(order.getPayment());
            order1.setCustomer(order.getCustomer());
            order1.setStatus(order.getStatus());
            if(order.getStatus().equals(Order.OrderStatus.DONE)) {
                order1.getCustomer().setBonusPoints(order1.getCustomer().getBonusPoints() + order1.getTotalAmount()*0.005);
            }
            orderRepository.save(order1);
            return order1;
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }


    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
        log.info("Delete order: {}", order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        log.info("Delete order by id: {}", id);

    }

    @Override
    public Page<Order> findAllOrders(int page, int pageSize, String search) {
        if (search != null && !search.isEmpty()) {
            Pageable pageable = PageRequest.of(page, pageSize);
            return orderRepository.findAll(OrderSpecification.byCustomerName(search).and(OrderSpecification.byNotDeleted()), pageable);
        }
        return orderRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<OrdersDto> getPagedAllOrdersDto(int page, int pageSize) {
        return null;
    }

    @Override
    public Page<OrdersDto> getPagedAllOrdersDto(int page, int pageSize, String search) {
        return orderMapper.toDto(findAllOrders(page, pageSize, search));
    }

    @Override
    public Integer countAllOrders() {
        return orderRepository.countAllOrders();
    }

    @Override
    public Integer countTodayOrders() {
        return orderRepository.countTodayOrders();
    }

    @Override
    public Long calculateTotalSales() {
        return orderRepository.findAllDoneOrders();
    }

    @Override
    public Long calculateTodaySales() {
        return orderRepository.findAllDoneOrdersToday();
    }

    @Override
    public Order saveOrderFromDto(OrdersDto ordersDto) {
        if (ordersDto.getId() != null) {
            return orderRepository.findById(ordersDto.getId())
                    .map(existingOrder -> updateOrder(existingOrder, ordersDto))
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + ordersDto.getId()));
        } else {
            Order newOrder = createOrder(ordersDto);
            return orderRepository.save(newOrder);
        }
    }

    @Override
    public List<OrdersDto> getLastOrdersForStatistics() {
        List<Order> orders = orderRepository.findAll();
        Stream<Order> orderStream = orders.stream().map(orderRepository::save);
        return orderMapper.toDto(orderRepository.getLastOrdersForStatistics());
    }

    @Override
    public List<Order> CancelAllOrdersByCustomer(Customer customer1) {
        List<Order> orders = orderRepository.findByCustomerAndStatusNot(customer1, Order.OrderStatus.DONE);

        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }

        orders.forEach(order -> {
            order.setStatus(Order.OrderStatus.CANCELLED);
            if (order.getDelivery() != null) {
                order.getDelivery().setStatus(Delivery.DeliveryStatus.CANCELLED);
            }
        });
        return orderRepository.saveAll(orders);
    }

    private Order updateOrder(Order existingOrder, OrdersDto ordersDto) {
        applyOrderDtoToOrder(existingOrder, ordersDto);

        Order savedOrder = orderRepository.save(existingOrder);

        Set<Long> updatedOrderItemIds = ordersDto.getOrderItemsDto().stream()
                .map(OrderItemDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<OrderItem> currentOrderItems = savedOrder.getOrderItems();

        List<OrderItem> itemsToRemove = currentOrderItems.stream()
                .filter(item -> !updatedOrderItemIds.contains(item.getId()))
                .collect(Collectors.toList());

        if (!itemsToRemove.isEmpty()) {

            savedOrder.getOrderItems().removeAll(itemsToRemove);
            boolean deletedNonexistent = orderItemService.deleteAll(itemsToRemove);
        }

        List<OrderItem> orderItems = ordersDto.getOrderItemsDto().stream()
                .map(orderItemDto -> orderItemService.saveOrderItem(orderItemDto, savedOrder))
                .collect(Collectors.toList());

        savedOrder.setOrderItems(orderItems);
        return orderRepository.save(savedOrder);
    }


    private Order createOrder(OrdersDto ordersDto) {
        Order newOrder = new Order();
        applyOrderDtoToOrder(newOrder, ordersDto);
        List<OrderItem> orderItems = ordersDto.getOrderItemsDto().stream()
                .map(orderItemDto -> orderItemService.saveOrderItem(orderItemDto, newOrder))
                .collect(Collectors.toList());
        newOrder.setOrderItems(orderItems);
        if(newOrder.getStatus().equals(Order.OrderStatus.DONE)) {
            newOrder.getCustomer().setBonusPoints(newOrder.getCustomer().getBonusPoints() + newOrder.getTotalAmount()*0.005);
        }
        return newOrder;
    }

    private void applyOrderDtoToOrder(Order order, OrdersDto ordersDto) {
        order.setDateTimeOfCreate(ordersDto.getDateTimeOfCreate());
        order.setDateTimeOfReady(ordersDto.getDateTimeOfReady());
        if (ordersDto.getDeliveryDto() != null) {
            order.setDelivery(deliveryService.saveDelivery(ordersDto.getDeliveryDto()));
        } else {
            order.setDelivery(null);
        }
        order.setPayment(ordersDto.getPayment());
        order.setStatus(ordersDto.getStatus());
    }
}
