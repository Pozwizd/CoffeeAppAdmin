package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.mapper.LocationMapper;
import com.spacelab.coffeeapp.service.CityService;
import com.spacelab.coffeeapp.service.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RequestMapping("/location")
@AllArgsConstructor
@Controller
public class LocationController {

    private final LocationService locationService;
    private final CityService cityService;
    private final LocationMapper locationMapper;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "locations");
        model.addAttribute("title", "Локации");
    }

    @GetMapping("/cities")
    public @ResponseBody List<City> getCities() {
        return cityService.findAllCities();
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("location/location");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<LocationDto> getEntities(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestParam(defaultValue = "5") Integer size) {
        if (search.isEmpty()) {
            return locationMapper.toDtoListPage(locationService.findAllLocations(page, size));
        } else {
            return locationMapper.toDtoListPage(locationService.findLocationsByRequest(page, size, search));
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public LocationDto getEntity(@PathVariable Long id) {
        return locationMapper.toDto(locationService.getLocation(id));
    }

    @PostMapping({"/", ""})
    @ResponseBody
    public ResponseEntity<?> createEntity(@RequestBody LocationDto location) {
        locationService.saveLocation(locationMapper.toEntity(location));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateEntity(@PathVariable Long id, @Valid @RequestBody LocationDto location)
    {
        locationService.updateLocation(id, locationMapper.toEntity(location));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok().build();
    }
}
