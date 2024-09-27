package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.entity.Location;
import com.spacelab.coffeeapp.mapper.LocationMapper;
import com.spacelab.coffeeapp.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class LocationServiceImpTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImp locationService;

    private Location location;
    private LocationDto locationDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        location = new Location();
        City city = new City();
        city.setId(1L);
        city.setName("Test City");
        location.setCity(city);
        location.setId(1L);
        location.setCity(city);

        locationDto = new LocationDto();
        locationDto.setId(1L);
        locationDto.setCity("Test City");
    }

    @Test
    void testSaveLocation() {
        locationService.saveLocation(location);
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void testSaveFromLocationDto() {
        when(locationMapper.toEntity(any(LocationDto.class))).thenReturn(location);
        locationService.saveFromLocationDto(locationDto);
        verify(locationMapper, times(1)).toEntity(locationDto);
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void testGetLocation() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Location foundLocation = locationService.getLocation(1L);
        assertEquals(location, foundLocation);
    }

    @Test
    void testGetLocation_NotFound() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> locationService.getLocation(1L));
    }

    @Test
    void testGetLocationDto() {
        when(locationMapper.toDto(any(Location.class))).thenReturn(locationDto);
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        LocationDto foundLocationDto = locationService.getLocationDto(1L);
        assertEquals(locationDto, foundLocationDto);
    }

    @Test
    void testGetAllLocations() {
        when(locationRepository.findAll()).thenReturn(List.of(location));
        List<Location> locations = locationService.getAllLocations();
        assertEquals(1, locations.size());
    }

    @Test
    void testUpdateLocation() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        City city2 = new City();
        city2.setId(2L);
        city2.setName("Updated City");
        location.setCity(city2);
        locationService.updateLocation(1L, location);
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void testUpdateLocation_NotFound() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> locationService.updateLocation(1L, location));
    }

    @Test
    void testUpdateLocationFromDto() {
        when(locationMapper.toEntity(any(LocationDto.class))).thenReturn(location);
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        locationDto.setCity("Updated City");
        locationService.updateLocationFromDto(1L, locationDto);
        verify(locationMapper, times(1)).toEntity(locationDto);
        verify(locationRepository, times(1)).save(location);
    }


    @Test
    void testDeleteLocation_Entity() {
        locationService.deleteLocation(location);
        verify(locationRepository, times(1)).delete(location);
    }

    @Test
    void testDeleteLocation_ById() {
        locationService.deleteLocation(1L);
        verify(locationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllLocations() {
        Page<Location> locationPage = new PageImpl<>(List.of(location));
        when(locationRepository.findAll(any(Pageable.class))).thenReturn(locationPage);

        Page<Location> result = locationService.findAllLocations(0, 10);
        assertNotNull(result);
    }

    @Test
    void testFindLocationsByRequest() {
        Page<Location> locationPage = new PageImpl<>(List.of(location));
        when(locationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(locationPage);

        Page<Location> result = locationService.findLocationsByRequest(0, 10, "Test City");
        assertNotNull(result);
    }

    @Test
    void testGetPagedAllLocationsDto_WithSearch() {
        Page<Location> locationPage = new PageImpl<>(List.of(location));
        Page<LocationDto> locationDtoPage = new PageImpl<>(List.of(locationDto));
        when(locationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(locationPage);
        when(locationMapper.toDto(any(Location.class))).thenReturn(locationDto);

        Page<LocationDto> result = locationService.getPagedAllLocationsDto(0, 10, "Test City");
        assertNotNull(result);
    }

    @Test
    void testGetPagedAllLocationsDto_WithoutSearch() {
        Page<Location> locationPage = new PageImpl<>(List.of(location));
        Page<LocationDto> locationDtoPage = new PageImpl<>(List.of(locationDto));
        when(locationRepository.findAll(any(Pageable.class))).thenReturn(locationPage);
        when(locationMapper.toDto(any(Location.class))).thenReturn(locationDto);

        Page<LocationDto> result = locationService.getPagedAllLocationsDto(0, 10, "");
        assertNotNull(result);
    }

    @Test
    void testCountLocations() {
        when(locationRepository.count()).thenReturn(1L);
        long count = locationService.countLocations();
        assertEquals(1, count);
    }


}