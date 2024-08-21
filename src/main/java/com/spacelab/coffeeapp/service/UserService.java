package com.spacelab.coffeeapp.service;


import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public void saveUser(User user);

    void saveUser(UserDto user);

    public User getUser(Long id);
    public List<User> getAllUsers();
    public void updateUser(Long id, UserDto user);
    public void deleteUser(User user);
    public void deleteUser(Long id);

    Page<UserDto> getUsersDtosByRequest(int page,
                                        int pageSize,
                                        String search);

    Page<User> findAllUsers(int page, int pageSize);

    Page<User> findUsersByRequest(int page, int pageSize, String search);

    Optional<User> getUserByEmail(String name);

    Boolean existsByEmail(String email);

    int countUsers();
}
