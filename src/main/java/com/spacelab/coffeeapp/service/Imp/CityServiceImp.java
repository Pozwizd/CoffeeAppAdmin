package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.repository.CityRepository;
import com.spacelab.coffeeapp.service.CityService;
import lombok.AllArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CityServiceImp implements CityService  {

    private final CityRepository cityRepository;
    @Override
    public void saveCity(City city) {
        cityRepository.save(city);
    }

    @Override
    public Optional<City> getCity(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public void updateCity(City city) {
        cityRepository.save(city);
    }

    @Override
    public void deleteCity(City city) {
        cityRepository.delete(city);

    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    @Override
    public Page findAllCities(int page, int pageSize) {
        return null;
    }

    @Override
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getCityByName(String name) {
        return cityRepository.findCityByName(name);
    }

    @Override
    public Page findCitiesByRequest(int page, int pageSize, String search) {
        return null;
    }
}
