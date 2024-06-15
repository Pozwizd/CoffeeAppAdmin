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
@RequestMapping("/products")
public class ProductController {

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "products");
        model.addAttribute("title", "Продукты");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("products/products");
    }

}
