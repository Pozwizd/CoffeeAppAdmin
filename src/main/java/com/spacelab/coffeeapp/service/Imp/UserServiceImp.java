package com.spacelab.coffeeapp.service.Imp;


import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.mapper.UserMapper;
import com.spacelab.coffeeapp.repository.UserRepository;
import com.spacelab.coffeeapp.service.UserService;
import com.spacelab.coffeeapp.specification.UserSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public void saveUser(User user) {
        log.info("Save user: {}", user);
        userRepository.save(user);

    }

    @Override
    public void saveUser(UserDto user) {
        log.info("Save user from dto: {}", user);
        saveUser(userMapper.toEntity(user));
    }

    @Override
    public User getUser(Long id) {
        log.info("Get user by id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("UserId " + id + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Get all users");
        return userRepository.findAll();
    }

    @Override
    public void updateUser(Long id, UserDto user) {
        userRepository.findById(id).map(user1 -> {
            user1.setName(user.getName());
            user1.setEmail(user.getEmail());
            if (user.getRole() != null){
                user1.setRole(User.Role.valueOf(user.getRole()));
            }
            user1.setPassword(user.getPassword());
            userRepository.save(user1);
            return user1;
        }).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Override
    public void deleteUser(User user) {
        log.info("Delete user: {}", user);
        userRepository.delete(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Delete user by id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public Page<UserDto> getUsersDtosByRequest(int page,
                                               int pageSize,
                                               String search) {

        if (search.isEmpty()) {
            return userMapper.toDtoListPage(findAllUsers(page, pageSize));
        } else {
            return userMapper.toDtoListPage(findUsersByRequest(page, pageSize, search));
        }
    }

    @Override
    public Page<User> findAllUsers(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get all users with pageable: {}", pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findUsersByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get all users by request: {}", search);
        return userRepository.findAll(UserSpecification.search(search), pageable);
    }

    @Override
    public Optional<User> getUserByEmail(String name) {
        log.info("Get user by email: {}", name);
        return userRepository.findUserByEmail(name);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public int countUsers() {
        return (int) userRepository.count();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Get UserDetails for Spring Security by email: {}", email);
        return userRepository
                .findUserByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username " + email + " not found"));
    }
}
