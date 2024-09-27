package com.spacelab.coffeeapp.service;



import com.spacelab.coffeeapp.entity.City;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CityService {

    void saveCity(City city);
    City getCity(Long id);
    void updateCity(City city);
    void deleteCity(City city);
    void deleteCity(Long id);

    List<City> findAllCities();
    City getCityByName(String name);
    Page<City> findCitiesByRequest(int page, int pageSize, String search);

    long countCities();
}
