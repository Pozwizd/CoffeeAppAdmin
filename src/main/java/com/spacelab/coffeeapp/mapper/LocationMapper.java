package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.entity.City;
import com.spacelab.coffeeapp.entity.Location;
import com.spacelab.coffeeapp.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationMapper {

    private final CityService cityService;

    public Page<LocationDto> toDtoListPage(Page<Location> locationPage) {
        List<LocationDto> locationDtoList = new ArrayList<>();
        for (Location l : locationPage.getContent()) {
            locationDtoList.add(toDto(l));
        }
        return new PageImpl<>(locationDtoList, locationPage.getPageable(), locationPage.getTotalElements());
    }

    public List<Location> toEntityListPage(List<LocationDto> locationList) {
        List<Location> locationDtoList = new ArrayList<>();
        for (LocationDto l : locationList) {
            locationDtoList.add(toEntity(l));
        }
        return locationDtoList;
    }

    public LocationDto toDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setCity(location.getCity().getName());
        locationDto.setLatitude(location.getLatitude());
        locationDto.setLongitude(location.getLongitude());
        locationDto.setStreet(location.getStreet());
        locationDto.setBuilding(location.getBuilding());
        return locationDto;
    }

    public Location toEntity(LocationDto locationDto) {
        Location location = new Location();
        location.setId(locationDto.getId());
        location.setCity(cityService.getCityByName(locationDto.getCity()));
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setStreet(locationDto.getStreet());
        location.setBuilding(locationDto.getBuilding());
        return location;
    }

}
