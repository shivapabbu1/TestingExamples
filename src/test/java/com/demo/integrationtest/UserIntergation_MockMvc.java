package com.demo.integrationtest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.Controller.UserController;
import com.demo.Service.UserService;
import com.demo.entity.User;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = new User(1L, "pabbu", "pabbu@gmail.com");
        user2 = new User(2L, "kommu", "kommu@gmail.com");
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].name").value(user1.getName()))
               .andExpect(jsonPath("$[1].name").value(user2.getName()));
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(user1.getId())).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/users/{id}", user1.getId()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value(user1.getName()));
    }

    @Test
    void createUser() throws Exception {
        User newUser = new User(null, "Alluri", "Alluri@gmail.com");
        when(userService.createUser(newUser)).thenReturn(user1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newUser)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value(user1.getName()));
    }

    @Test
    void updateUser() throws Exception {
        User updatedUser = new User(user1.getId(), "Updated Pabbu", "updated.pabbu@gmail.com");
        when(userService.updateUser(user1.getId(), updatedUser)).thenReturn(updatedUser);

        mockMvc.perform(put("/users/{id}", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUser)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value(updatedUser.getName()));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", user1.getId()))
               .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}