package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    void saveLocation(Location location);

    void saveFromLocationDto(LocationDto locationDto);

    Location getLocation(Long id);
    LocationDto getLocationDto(Long id);

    List<Location> getAllLocations();
    void updateLocation(Long id, Location location);
    void updateLocationFromDto(Long id, LocationDto locationDto);
    void deleteLocation(Location location);
    void deleteLocation(Long id);
    Page<Location> findAllLocations(int page, int pageSize);
    Page<Location> findLocationsByRequest(int page, int pageSize, String search);

    Page<LocationDto> getPagedAllLocationsDto(int page, int pageSize, String search);

    long countLocations();
}
