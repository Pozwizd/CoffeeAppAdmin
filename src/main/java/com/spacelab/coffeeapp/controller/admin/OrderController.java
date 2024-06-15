package com.spacelab.coffeeapp.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "orders");
        model.addAttribute("title", "Заказы");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("orders/orders");
    }


}
