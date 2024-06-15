package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.Location;
import com.spacelab.coffeeapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface LocationService {
    void saveLocation(Location location);
    Location getLocation(Long id);
    List<Location> getAllLocations();
    void updateLocation(Long id, Location location);
    void deleteLocation(Location location);
    void deleteLocation(Long id);
    Page<Location> findAllLocations(int page, int pageSize);
    Page<Location> findLocationsByRequest(int page, int pageSize, String search);

}
