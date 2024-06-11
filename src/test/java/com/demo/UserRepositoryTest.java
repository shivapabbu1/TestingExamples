package com.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.demo.entity.User;
import com.demo.repo.UserRepository;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

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
        User newUser = repository.save(user1);

        // Assert
        assertNotNull(newUser);
        assertThat(newUser.getId()).isNotEqualTo(null);
    }

}
