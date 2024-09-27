package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.service.CityService;
import com.spacelab.coffeeapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final CityService cityService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "order");
        model.addAttribute("title", "Заказы");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("orders/orders");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<OrdersDto> getAllOrders(@RequestParam(defaultValue = "0") int page,
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
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ModelAndView getOrder(@PathVariable String id) {
        return new ModelAndView("orders/orderItem").addObject("order", orderService.getOrderDto(Long.valueOf(id)));
    }

    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> saveOrder(@Valid @RequestBody OrdersDto ordersDto) {
        Order order = orderService.saveOrderFromDto(ordersDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/typePayment")
    @ResponseBody
    public List<Order.Payment> getTypePayment() {
        return Arrays.stream(Order.Payment.values()).toList();
    }

    @GetMapping("/status")
    @ResponseBody
    public List<Order.OrderStatus> getStatus() {
        return Arrays.stream(Order.OrderStatus.values()).toList();
    }

    @GetMapping("/getCities")
    @ResponseBody
    public List<City> getCities() {
        return cityService.findAllCities();
    }

    @GetMapping("/getStatusDelivery")
    @ResponseBody
    public List<Delivery.DeliveryStatus> getStatusDelivery() {
        return Arrays.stream(Delivery.DeliveryStatus.values()).toList();
    }
}
