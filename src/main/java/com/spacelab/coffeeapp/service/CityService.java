package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.City;
import org.hibernate.query.Page;

import java.util.List;
import java.util.Optional;

public interface CityService {

    void saveCity(City city);
    Optional<City> getCity(Long id);
    void updateCity(City city);
    void deleteCity(City city);
    void deleteCity(Long id);
    Page findAllCities(int page, int pageSize);
    List<City> findAllCities();
    City getCityByName(String name);
    Page findCitiesByRequest(int page, int pageSize, String search);

}
