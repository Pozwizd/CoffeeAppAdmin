package com.spacelab.coffeeapp.controller.admin;

import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.OrderMapper;
import com.spacelab.coffeeapp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

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
                                       @RequestParam(defaultValue = "5") Integer size) {

        return orderMapper.toDto(orderService.findAllOrders(page, size));
    }

    @GetMapping("/{id}")
    public ModelAndView getEntity(@PathVariable String id, HttpSession session,  Model model) {


        OrdersDto ordersDto = orderMapper.toDto(orderService.getOrder(Long.valueOf(id)));
        model.addAttribute("orders", ordersDto);

        return new ModelAndView("orders/orderItem");

    }

    @PostMapping("/{id}")
    public ModelAndView updateEntity(@PathVariable String id,
                                     HttpSession session,
                                     BindingResult bindingResult,
                                     Model model) {

        List<FieldError> filteredErrors = bindingResult.getFieldErrors().stream()
                .filter(error -> !error.getField().equals("id"))
                .collect(Collectors.toList());
        if (filteredErrors.size() > 0) {
        }
        return new ModelAndView("");
    }



    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
