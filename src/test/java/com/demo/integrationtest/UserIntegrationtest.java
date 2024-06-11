package com.demo.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import com.demo.Controller.UserController;
import com.demo.entity.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationtest {
    
    @Autowired
    public UserController userController;

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    private User user1;
    private User user2;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setup() {
        baseUrl = baseUrl + ":" + port + "/users";

        // Initialize and save user1
        user1 = new User();
        user1.setName("shiva pabbu");
        user1.setEmail("shiva.pabbu@example.com");
        user1 = restTemplate.postForObject(baseUrl, user1, User.class);

        // Initialize and save user2
        user2 = new User();
        user2.setName("kiran kumar");
        user2.setEmail("kiran.kumar@example.com");
        user2 = restTemplate.postForObject(baseUrl, user2, User.class);
    }

//    @AfterEach
//    public void tearDown() {
//        restTemplate.delete(baseUrl + "/" + user1.getId());
//        restTemplate.delete(baseUrl + "/" + user2.getId());
//    }

    @Test
    void getAllUserTest() {
        List<User> getAllUsers = restTemplate.getForObject(baseUrl, List.class);
        assertThat(getAllUsers).isNotNull();
        assertThat(getAllUsers.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void saveUserTest() {
        User user = new User();
        user.setName("new user");
        user.setEmail("new.user@example.com");

        User savedUser = restTemplate.postForObject(baseUrl, user, User.class);
        List<User> getAllUsers = restTemplate.getForObject(baseUrl, List.class);
        assertNotNull(savedUser);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(getAllUsers.size()).isEqualTo(5);
    }

    @Test
    void getUserByIdTest() {
        User retrievedUser = restTemplate.getForObject(baseUrl + "/" + user1.getId(), User.class);
        assertNotNull(retrievedUser);
        assertThat(retrievedUser.getId()).isEqualTo(user1.getId());
    }

    @Test
    void updateUserTest() {
        user1.setName("Shiva Updated");
        restTemplate.put(baseUrl + "/" + user1.getId(), user1);

        User updatedUser = restTemplate.getForObject(baseUrl + "/" + user1.getId(), User.class);
        assertNotNull(updatedUser);
        assertThat(updatedUser.getName()).isEqualTo("Shiva Updated");
    }

    
}