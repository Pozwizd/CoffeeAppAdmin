package com.spacelab.coffeeapp.controller;


import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @GetMapping({"/", ""})
    public @ResponseBody List<City> getCities() {
        return cityService.findAllCities();
    }

}
