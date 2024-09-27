package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.mapper.UserMapper;
import com.spacelab.coffeeapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImp userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
    }

    @Test
    void testSaveUser_Entity() {
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUser_Dto() {
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        userService.saveUser(userDto);
        verify(userMapper, times(1)).toEntity(userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        User foundUser = userService.getUser(1L);
        assertEquals(user, foundUser);
    }

    @Test
    void testGetUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userDto.setName("Updated Name");
        userService.updateUser(1L, userDto);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_WithRole() {
        userDto.setName("Updated Name");
        userDto.setRole("MANAGER");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.updateUser(1L, userDto);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userDto));
    }

    @Test
    void testDeleteUser_Entity() {
        userService.deleteUser(user);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_ById() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetUsersDtosByRequest() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toDtoListPage(any(Page.class))).thenReturn(new PageImpl<>(List.of(userDto)));

        Page<UserDto> result = userService.getUsersDtosByRequest(0, 10, "Example");
        assertNotNull(result);
    }

    @Test
    void testGetUsersDtosByRequest_EmptySearch() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toDtoListPage(any(Page.class))).thenReturn(new PageImpl<>(List.of(userDto)));

        Page<UserDto> result = userService.getUsersDtosByRequest(0, 10, "");
        assertNotNull(result);
    }

    @Test
    void testFindAllUsers() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.findAllUsers(0, 10);
        assertNotNull(result);
    }

    @Test
    void testFindUsersByRequest() {
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(userPage);

        Page<User> result = userService.findUsersByRequest(0, 10, "test");
        assertNotNull(result);
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        boolean exists = userService.existsByEmail("test@example.com");
        assertTrue(exists);
    }

    @Test
    void testCountUsers() {
        when(userRepository.count()).thenReturn(1L);
        int count = userService.countUsers();
        assertEquals(1, count);
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername("test@example.com");
        assertNotNull(userDetails);
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("test@example.com"));
    }
}
