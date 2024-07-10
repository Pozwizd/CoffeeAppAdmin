package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.Location;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.repository.LocationRepository;
import com.spacelab.coffeeapp.service.CityService;
import com.spacelab.coffeeapp.service.LocationService;
import com.spacelab.coffeeapp.specification.LocationSpecification;
import com.spacelab.coffeeapp.specification.UserSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LocationServiceImp implements LocationService {
    private final LocationRepository locationRepository;
    private final CityService cityService;

    @Override
    public void saveLocation(Location location) {
        log.info("Save location: {}", location);
        locationRepository.save(location);
    }

    @Override
    public Location getLocation(Long id) {
        log.info("Get location by id: {}", id);
        return locationRepository.findById(id).orElseThrow(() -> new RuntimeException("Location not found"));
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }


    @Override
    public void updateLocation(Long id, Location location) {
        locationRepository.findById(id).map(location1 -> {
            location1.setCity(location.getCity());
            location1.setLatitude(location.getLatitude());
            location1.setLongitude(location.getLongitude());
            location1.setStreet(location.getStreet());
            location1.setBuilding(location.getBuilding());

            locationRepository.save(location1);
            return location1;
        }).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Override
    public void deleteLocation(Location location) {
        log.info("Delete location: {}", location);
        locationRepository.delete(location);
    }

    @Override
    public void deleteLocation(Long id) {
        log.info("Delete location by id: {}", id);
        locationRepository.deleteById(id);
    }

    @Override
    public Page<Location> findAllLocations(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get locations with pageable: {}", pageable);
        return locationRepository.findAll(pageable);
    }

    @Override
    public Page<Location> findLocationsByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Location> specification = new LocationSpecification(search);
        log.info("Get all users by request: {}", search);
        return locationRepository.findAll(specification, pageable);
    }

    @Override
    public long countLocations() {
        return locationRepository.count();
    }
}
