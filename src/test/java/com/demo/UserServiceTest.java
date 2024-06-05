package com.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.demo.Service.UserService;
import com.demo.entity.User;
import com.demo.repo.UserRepository;

@WebMvcTest(User.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private List<User> userList;

    @BeforeEach
    void init() {
        userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setName("shiva");
        user1.setEmail("shiva@gmail.com");
        userList.add(user1);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("kiran");
        user2.setEmail("kiran@example.com");
        userList.add(user2);
    }

    @Test
    void saveUser() {
        // Arrange
        User user =userList.get(0);

        // Act
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.createUser(user);

        // Assert
        assertNotNull(savedUser);
        assertThat(savedUser.getName()).isEqualTo(user.getName());
    }

    @Test
    void getAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void getUserById() {
        // Arrange
        User user = userList.get(0);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        Optional<User> retrievedUser = userService.getUserById(user.getId());

        // Assert
        assertThat(retrievedUser).isNotEmpty();
        assertThat(retrievedUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void getUserByEmail() {
        // Arrange
        User user = userList.get(0);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        Optional<User> retrievedUser = userService.findByEmail(user.getEmail());

        // Assert
        assertThat(retrievedUser).isNotEmpty();
        assertThat(retrievedUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void deleteUser() {
        // Arrange
        User user = userList.get(0);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(user.getId());

        // Assert
        // No need to assert, just make sure no exceptions are thrown
    }
}
