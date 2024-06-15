package com.spacelab.coffeeapp.service.Imp;


import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.Role;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.repository.UserRepository;
import com.spacelab.coffeeapp.service.UserService;
import com.spacelab.coffeeapp.specification.UserSpecification;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(UserServiceImp.class);
    private final PasswordEncoder passwordEncoder;



    @Override
    public User saveUser(User user) {
        logger.info("Save user: {}", user);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        logger.info("Get user by id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("UserId " + id + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Get all users");
        return userRepository.findAll();
    }

    @Override
    public void updateUser(Long id, UserDto user) {
        userRepository.findById(id).map(user1 -> {
            user1.setName(user.getName());
            user1.setEmail(user.getEmail());
            user1.setRole(Role.valueOf(user.getRole()));
            user1.setPassword(user.getPassword());
            userRepository.save(user1);
            return user1;
        }).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Override
    public void deleteUser(User user) {
        logger.info("Delete user: {}", user);
        userRepository.delete(user);
    }
    @Override
    public void deleteUser(Long id) {
        logger.info("Delete user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> findAllUsers(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        logger.info("Get all users with pageable: {}", pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findUsersByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<User> specification = new UserSpecification(search);
        logger.info("Get all users by request: {}", search);
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public User getUserByEmail(String name) {
        logger.info("Get user by email: {}", name);
        return userRepository
                .findUserByEmail(name)
                .orElseThrow(
                        () -> new UsernameNotFoundException(null));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Get UserDetails for Spring Security by email: {}", email);
        return userRepository
                .findUserByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username " + email + " not found"));
    }
}
