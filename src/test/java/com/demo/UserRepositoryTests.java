package com.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.demo.entity.User;
import com.demo.repo.UserRepository;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void init() {
        user1 = new User();
        user1.setName("shiva");
        user1.setEmail("shiva@gmail.com");

        user2 = new User();
        user2.setName("kiran");
        user2.setEmail("kiran@example.com");
    }

    @Test
    void saveUser() {
        // Arrange

        // Act
        User newUser = userRepository.save(user1);

        // Assert
        assertNotNull(newUser);
        assertThat(newUser.getId()).isNotEqualTo(null);
    }

    @Test
    void getAllUsers() {
        // Arrange
        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void getUserById() {
        // Arrange
        User savedUser = userRepository.save(user1);

        // Act
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(retrievedUser.isPresent());
        assertThat(retrievedUser.get().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void getUserByEmail() {
        // Arrange
        User savedUser = userRepository.save(user1);

        // Act
        Optional<User> retrievedUser = userRepository.findByEmail(savedUser.getEmail());

        // Assert
        assertTrue(retrievedUser.isPresent());
        assertThat(retrievedUser.get().getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    void updateUser() {
        // Arrange
        User savedUser = userRepository.save(user1);

        // Act
        savedUser.setName("Shiva Updated");
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertNotNull(updatedUser);
        assertThat(updatedUser.getName()).isEqualTo("Shiva Updated");
    }

    @Test
    void deleteUser() {
        // Arrange
        User savedUser = userRepository.save(user1);

        // Act
        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        // Assert
        assertThat(deletedUser).isEmpty();
    }
}
