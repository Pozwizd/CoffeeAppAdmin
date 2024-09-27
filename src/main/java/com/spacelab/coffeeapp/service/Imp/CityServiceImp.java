package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.repository.CityRepository;
import com.spacelab.coffeeapp.service.CityService;
import com.spacelab.coffeeapp.specification.CitySpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CityServiceImp implements CityService  {

    private final CityRepository cityRepository;

    @Override
    public void saveCity(City city) {
        cityRepository.save(city);
    }

    @Override
    public City getCity(Long id) {
        return cityRepository.findById(id).orElse(null);
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
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public City getCityByName(String name) {
        return cityRepository.findCityByName(name);
    }

    @Override
    public Page<City> findCitiesByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);

        if (search != null && !search.isEmpty()) {
            return cityRepository.findAll(CitySpecification.search(search), pageable);
        }
        return cityRepository.findAll(pageable);
    }

    @Override
    public long countCities() {
        return cityRepository.count();
    }
}
