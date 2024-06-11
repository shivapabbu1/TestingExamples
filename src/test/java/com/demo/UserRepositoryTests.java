package com.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.demo.entity.User;
import com.demo.repo.UserRepository;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(statements = {
            "INSERT INTO user (name, email) VALUES ('lucky', 'lucky@gmail.com')"
    })
    void testSaveUser() {
        // Arrange
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        // Act
        User savedUser = testEntityManager.persist(user);
        testEntityManager.flush(); // Flush to synchronize the persistence context

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindAll() {
        // Arrange
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@example.com");
        testEntityManager.persist(user1);

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        testEntityManager.persist(user2);
        testEntityManager.flush(); // Flush to synchronize the persistence context

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);
    }
}
