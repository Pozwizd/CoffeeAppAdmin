package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.DeliveryDto;
import com.spacelab.coffeeapp.dto.OrderItemDto;
import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.service.AttributeValueService;
import com.spacelab.coffeeapp.service.CityService;
import com.spacelab.coffeeapp.service.DeliveryService;
import com.spacelab.coffeeapp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final CityService cityService;
    private final AttributeValueService attributeValueService;


    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "orders");
        model.addAttribute("title", "Заказы");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("orders/orders");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<OrdersDto> getEntities(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") Integer size,
                                       @RequestParam(defaultValue = "") String search) {

        return orderService.getPagedAllOrdersDto(page, size, search);
    }

    @GetMapping("/getLastOrdersForStatistics")
    @ResponseBody
    public List<OrdersDto> getLastOrdersForStatistics() {

        return orderService.getLastOrdersForStatistics();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id, HttpSession session) {
        orderService.deleteOrder(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ModelAndView getEntity(@PathVariable String id) {
        OrdersDto order = orderService.getOrderDto(Long.valueOf(id));
        return new ModelAndView("orders/orderItem").addObject("order", orderService.getOrderDto(Long.valueOf(id)));
    }

    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> saveEntity(@Valid @RequestBody OrdersDto ordersDto, HttpSession session) {
        OrdersDto order = ordersDto;
        Order order1 = orderService.saveOrderFromDto(ordersDto);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/fromSession")
    public OrdersDto getOrderFomDtom(HttpSession session) {
        OrdersDto order = (OrdersDto) session.getAttribute("order");
        session.setAttribute("order", order);
        return order;
    }

    @ResponseBody
    @GetMapping("/items")
    public List<OrderItemDto> getItems(HttpSession session) {

        OrdersDto order = (OrdersDto) session.getAttribute("order");
        session.setAttribute("order", order);
        List<OrderItemDto> items = order.getOrderItemsDto();
        return items;
    }

    // Загрузка элемента заказа в модальное окно
    @ResponseBody
    @GetMapping("/item/{id}")
    public OrderItemDto getItem(HttpSession session, @PathVariable String id) {

         OrdersDto order = (OrdersDto) session.getAttribute("order");
        return order.getOrderItemsDto().stream().filter(orderItemDto -> orderItemDto.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
    }

    @ResponseBody
    @PutMapping("/item/{id}")
    public ResponseEntity<?> editItem(@Valid @RequestBody OrderItemDto orderItemDto, HttpSession session, @PathVariable String id) {

        OrdersDto order = (OrdersDto) session.getAttribute("order");
        order.getOrderItemsDto().stream().filter(orderItemDtoFromSession ->
                orderItemDto.getId().equals(Long.valueOf(id))).findFirst().map(orderItemDto1 -> {
            orderItemDto1.setCategoryId(orderItemDto.getCategoryId());
            orderItemDto1.setProductId(orderItemDto.getProductId());
            orderItemDto1.setQuantity(orderItemDto.getQuantity());
            for (int i = 0; i < orderItemDto.getAttributes().size(); i++) {
                orderItemDto1.getAttributes().get(i).setAttributeValueId(orderItemDto.getAttributes().get(i).getAttributeValueId());
                orderItemDto1.getAttributes().get(i).setProductAttributeId(orderItemDto.getAttributes().get(i).getProductAttributeId());
            }
            return null;
        });

        order = recalculateAmount(order);
        session.setAttribute("order", order);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/item/create")
    public ResponseEntity<?> saveItem(@RequestBody OrderItemDto items ,HttpSession session) {

        OrdersDto order = (OrdersDto) session.getAttribute("order");
        List<OrderItemDto> items1 = new ArrayList<>();

        order = recalculateAmount(order);
        session.setAttribute("order", order);
        return ResponseEntity.ok().build();
    }

    // Удаление элемента заказа из сессии
    @DeleteMapping("/item/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id, HttpSession session) {
        OrdersDto order = (OrdersDto) session.getAttribute("order");
        order.getOrderItemsDto().removeIf(orderItemDto -> orderItemDto.getId().equals(id));
        order = recalculateAmount(order);
        session.setAttribute("order", order);
        return ResponseEntity.ok().build();
    }


    // Загрузка типов оплаты в модальное окно
    @GetMapping("/typePayment")
    @ResponseBody
    public List<Order.Payment> getTypePayment() {
        return Arrays.stream(Order.Payment.values()).toList();
    }

    // Загрузка статусов заказа в модальное окно
    @GetMapping("/status")
    @ResponseBody
    public List<Order.OrderStatus> getStatus() {
        return Arrays.stream(Order.OrderStatus.values()).toList();
    }

    // Загрузка полей заказа на страницу и в модальное окно
    @GetMapping("/details")
    @ResponseBody
    public OrdersDto getOrdersDto(@PathVariable String id, HttpSession session) {
        return (OrdersDto) session.getAttribute("order");
    }

    // Загрузка полей заказа в сессии
    @ResponseBody
    @PostMapping("/details")
    public ResponseEntity<?> savePayment(@RequestParam(name = "items" ) String items,
                                         @RequestParam(name = "delivery" ) String delivery,
                                         HttpSession session) {

        OrdersDto order = (OrdersDto) session.getAttribute("order");
        List<OrderItemDto> items1 = new ArrayList<>();
        return ResponseEntity.ok().build();
    }
    //=====================================

    // Загрузка типов оплаты в модальное окно
    @GetMapping("/getCities")
    @ResponseBody
    public List<City> getCities() {
        return cityService.findAllCities();
    }

    // Загрузка статусов доставки в модальное окно
    @GetMapping("/getStatusDelivery")
    @ResponseBody
    public List<Delivery.DeliveryStatus> getStatusDelivery() {
        return Arrays.stream(Delivery.DeliveryStatus.values()).toList();
    }

    // Загрузка типов оплаты в модальное окно
    @GetMapping("/delivery")
    @ResponseBody
    public DeliveryDto getDelivery(HttpSession session) {
        OrdersDto order = (OrdersDto) session.getAttribute("order");
        DeliveryDto deliveryDto = order.getDeliveryDto();
        return deliveryDto;
    }
    //=====================================

    public OrdersDto recalculateAmount(OrdersDto order) {
        double sum = order.getOrderItemsDto().stream()
                .mapToDouble(this::calculateOrderItemTotal)
                .sum();
        order.setTotalAmount(sum);
        return order;
    }

    private double calculateOrderItemTotal(OrderItemDto orderItemDto) {
        return orderItemDto.getQuantity() * orderItemDto.getAttributes().stream()
                .mapToDouble(attr -> attributeValueService.getAttributeValue(attr.getAttributeValueId()).get().getPrice())
                .sum();
    }

}
