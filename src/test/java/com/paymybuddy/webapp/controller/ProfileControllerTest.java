package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.security.SpringSecurityConfiguration;
import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.security.CustomOAuth2Service;
import com.paymybuddy.webapp.security.CustomUserDetailsService;
import com.paymybuddy.webapp.service.IUserProfileService;
import com.paymybuddy.webapp.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfileController.class)
@Import(SpringSecurityConfiguration.class)
public class ProfileControllerTest {
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private CustomOAuth2Service oAuth2Service;
    @MockBean
    private IUserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserProfileService userProfileService;
    @InjectMocks
    private UserController controller;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setEmail("john_doe@mail.com");
        userDTO.setPassword("ValidPassword@123");
        userDTO.setUsername("john_doe");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Given the new updates parameters are valid when update then update should be successful")
    public void updateUserSuccessTest() throws Exception {
        mockMvc.perform(post("/profile")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("message", "success"));

        verify(userProfileService, times(1)).updateUser(userDTO);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Given the new username isn't valid when update username then don't update")
    public void updateUserNameFailsTest() throws Exception {
        userDTO.setUsername(""); //invalid username

        mockMvc.perform(post("/profile")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "username"));

        verify(userProfileService, never()).updateUser(userDTO);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Given the new email isn't valid when update email then don't update")
    public void updateEmailFailsTest() throws Exception {
        userDTO.setEmail("not_valid@"); //invalid username
        mockMvc.perform(post("/profile")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "email"));

        verify(userProfileService, never()).updateUser(userDTO);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Given the new password isn't valid when update user then don't update")
    public void updatePasswordFailsTest() throws Exception {
        userDTO.setPassword("invalid");

        mockMvc.perform(post("/profile")
                        .with(csrf())
                        .param("username", userDTO.getUsername())
                        .param("email", userDTO.getEmail())
                        .param("password", userDTO.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "password"));

        verify(userProfileService, never()).updateUser(userDTO);
    }
}
