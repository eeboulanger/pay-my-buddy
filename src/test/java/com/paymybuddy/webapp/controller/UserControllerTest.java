package com.paymybuddy.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.webapp.security.SpringSecurityConfiguration;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.security.CustomOAuth2Service;
import com.paymybuddy.webapp.security.CustomUserDetailsService;
import com.paymybuddy.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import({SpringSecurityConfiguration.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private CustomOAuth2Service oAuth2Service;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @InjectMocks
    private UserController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private User user;

    @BeforeEach
    public void setUp(){
        user=new User();
        user.setUsername("Jane");
        user.setEmail("jane_doe@mail.com");
        user.setPassword("1234@Valid");
        user.setRole("USER");
    }

    @Test
    @DisplayName("Given authenticated as admin when create new should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createNewAccountAsAdmin_shouldSucceedTest() throws Exception {

        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Given authenticated as user when creating a new user should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void createNewUser_whenAuthenticatedAsUser_shouldFail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .with(csrf()))
                .andExpect(status().isForbidden());
        verify(userService, never()).saveUser(user);
    }


    @Test
    @DisplayName("Given authenticated as admin when delete user should succeed")
    @WithMockUser(roles="ADMIN")
    public void deleteUserAsAdmin_shouldSucceedTest() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Given authenticated as user when deleting a user should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void deleteUser_whenAuthenticatedAsUser_shouldFail() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteById(1);
    }

    @Test
    @DisplayName("Given authenticated as admin when update user should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateUserAsAdmin_shouldSucceedTest() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Given authenticated as user when update a user should fail")
    @WithMockUser(username = "user", roles = "USER")
    public void updateUser_whenAuthenticatedAsUser_shouldFail() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(userService, never()).saveUser(user);
    }

    @Test
    @DisplayName("Given authenticated as admin, get all user should succeed")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getAllUsersTest() throws Exception {
        List<User> list = List.of(user);
        when(userService.getAllUsers()).thenReturn(list);

        mockMvc.perform(get("/users")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$.[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.[0].password").value(user.getPassword()));

        verify(userService, times(1)).getAllUsers();


    }
}
