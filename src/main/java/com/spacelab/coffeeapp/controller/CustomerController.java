package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.*;
import com.spacelab.coffeeapp.service.CityService;
import com.spacelab.coffeeapp.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CityService cityService;
    private final CustomerService customerService;


    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "customers");
        model.addAttribute("title", "Клиенты");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("customers/customers");
    }

    @GetMapping("/cities")
    public @ResponseBody List<City> getCities() {
        return cityService.findAllCities();
    }

    @GetMapping("/customerStatus")
    public @ResponseBody List<CustomerStatus> getCustomerStatus() {
        return List.of(CustomerStatus.ACTIVE, CustomerStatus.NEW, CustomerStatus.BLOCKED);
    }

    @GetMapping("/getLanguages")
    public @ResponseBody List<Language> getLanguages() {
        return List.of(Language.EN, Language.RU);
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<CustomerDto> getEntities(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "") String search,
                                         @RequestParam(defaultValue = "5") Integer size) {
        return customerService.findCustomersPageByRequest(page, size, search);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CustomerDto getEntity(@PathVariable Long id) {
        return customerService.getCustomerDto(id);
    }

    @PostMapping({"/", ""})
    @ResponseBody
    public ResponseEntity<ResponseStatus> createEntity(@Valid @RequestBody CustomerDto customerDto) {
        customerService.saveCustomer(customerDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseStatus> updateEntity(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(id, customerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
