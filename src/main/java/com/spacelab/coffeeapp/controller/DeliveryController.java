package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.DeliveryDto;
import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.service.DeliveryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/delivery")
@AllArgsConstructor
@Controller
public class DeliveryController {

    private final DeliveryService deliveryService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "delivery");
        model.addAttribute("title", "Доставка");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("delivery/delivery");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<DeliveryDto> getEntities(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") Integer size) {
        return deliveryService.findAllDeliveryDtos(page, size);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public DeliveryDto getEntity(@PathVariable String id) {
        return deliveryService.getDeliveryDto(Long.valueOf(id));
    }
}
