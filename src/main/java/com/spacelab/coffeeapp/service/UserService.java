package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public void saveUser(User user);
    public User getUser(Long id);
    public List<User> getAllUsers();
    public void updateUser(User user);
    public void deleteUser(User user);

    Page<User> findAllUsers(int page, int pageSize);

    Page<User> findUsersByRequest(int page, int pageSize, String search);

    User getUserByEmail(String name);

    Boolean existsByEmail(String email);
}
