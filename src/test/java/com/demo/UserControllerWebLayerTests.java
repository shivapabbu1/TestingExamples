package com.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.demo.Service.UserService;
import com.demo.entity.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerWebLayerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User mockUser;

    @BeforeEach
    public void setup() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("shiva");
        mockUser.setEmail("shiva@example.com");

        // Mock the userService methods
    
     
    }

    @Test
    public void testCreateUser() throws Exception {
        
    	Mockito.when(userService.createUser(any(User.class))).thenReturn(mockUser);
        String content = objectMapper.writeValueAsString(mockUser);
        


        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("shiva"))
                .andExpect(jsonPath("$.email").value("shiva@example.com"));
    }

    @Test
    public void testGetUserById() throws Exception {
       
        Mockito.when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1" ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name").value("shiva"))
                .andExpect(jsonPath("$.email").value("shiva@example.com"));
    }
    @Test
    public void testGetAllUsers() throws Exception {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setName("John Doe");
        anotherUser.setEmail("john.doe@example.com");

        Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(mockUser, anotherUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value("shiva"))
                .andExpect(jsonPath("$[0].email").value("shiva@example.com"))
                .andExpect(jsonPath("$[1].name").value("John Doe"))
                .andExpect(jsonPath("$[1].email").value("john.doe@example.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {

        Mockito.doNothing().when(userService).deleteUser(1L);
        // Act
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
