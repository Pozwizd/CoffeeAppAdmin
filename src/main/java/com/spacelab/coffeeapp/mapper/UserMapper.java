package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.LocationDto;
import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.Location;
import com.spacelab.coffeeapp.entity.Role;
import com.spacelab.coffeeapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {

    public Page<UserDto> toDtoListPage(Page<User> locationPage) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User l : locationPage.getContent()) {
            userDtos.add(toDto(l));
        }
        return new PageImpl<>(userDtos, locationPage.getPageable(), locationPage.getTotalElements());
    }

    public List<User> toEntityListPage(List<UserDto> locationList) {
        List<User> users = new ArrayList<>();
        for (UserDto l : locationList) {
            users.add(toEntity(l));
        }
        return users;
    }

    public UserDto toDto(User location) {
        UserDto userDto = new UserDto();
        userDto.setId(location.getId());
        userDto.setName(location.getName());
        userDto.setEmail(location.getEmail());
        userDto.setRole(location.getRole().name());
        return userDto;
    }

    public User toEntity(UserDto locationDto) {
        User user = new User();
        user.setId(locationDto.getId());
        user.setName(locationDto.getName());
        user.setEmail(locationDto.getEmail());
        user.setRole(Role.valueOf(locationDto.getRole()));
        return user;
    }
}
