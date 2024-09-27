package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class CityServiceImpTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImp cityService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCity() {
        City city = new City();
        cityService.saveCity(city);
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    public void testGetCity() {
        City city = new City();
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        City result = cityService.getCity(1L);
        assertNotNull(result);
        assertEquals(city, result);
    }

    @Test
    public void testGetCityNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        City result = cityService.getCity(1L);
        assertNull(result);
    }

    @Test
    public void testUpdateCity() {
        City city = new City();
        cityService.updateCity(city);
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    public void testDeleteCity() {
        City city = new City();
        cityService.deleteCity(city);
        verify(cityRepository, times(1)).delete(city);
    }

    @Test
    public void testDeleteCityById() {
        cityService.deleteCity(1L);
        verify(cityRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAllCities() {
        City city1 = new City();
        City city2 = new City();
        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));

        List<City> result = cityService.findAllCities();
        assertEquals(2, result.size());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    public void testGetCityByName() {
        City city = new City();
        when(cityRepository.findCityByName("TestCity")).thenReturn(city);

        City result = cityService.getCityByName("TestCity");
        assertNotNull(result);
        assertEquals(city, result);
    }

    @Test
    public void testFindCitiesByRequestWithSearch() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<City> cityPage = new PageImpl<>(Arrays.asList(new City(), new City()));

        when(cityRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(cityPage);

        Page<City> result = cityService.findCitiesByRequest(0, 10, "searchQuery");
        assertEquals(2, result.getContent().size());
        verify(cityRepository, times(1)).findAll(any(Specification.class), eq(pageRequest));
    }

    @Test
    public void testFindCitiesByRequestWithoutSearch() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<City> cityPage = new PageImpl<>(Arrays.asList(new City(), new City()));

        when(cityRepository.findAll(eq(pageRequest))).thenReturn(cityPage);

        Page<City> result = cityService.findCitiesByRequest(0, 10, null);
        assertEquals(2, result.getContent().size());
        verify(cityRepository, times(1)).findAll(eq(pageRequest));
    }

    @Test
    public void testFindCitiesByRequestWithEmptySearch() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<City> cityPage = new PageImpl<>(Arrays.asList(new City(), new City()));

        when(cityRepository.findAll(eq(pageRequest))).thenReturn(cityPage);

        Page<City> result = cityService.findCitiesByRequest(0, 10, "");
        assertEquals(2, result.getContent().size());
        verify(cityRepository, times(1)).findAll(eq(pageRequest));
    }

    @Test
    public void testCountCities() {
        when(cityRepository.count()).thenReturn(5L);

        long result = cityService.countCities();
        assertEquals(5, result);
        verify(cityRepository, times(1)).count();
    }
}
