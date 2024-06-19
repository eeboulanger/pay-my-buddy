package com.paymybuddy.webapp.it;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.exception.ProfileException;
import com.paymybuddy.webapp.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ProfileServiceIT {

    @Autowired
    private UserProfileService profileService;

    @Test
    @WithMockUser(username = "john_doe@mail.com", password = "1234@Abcd")
    public void updateUser_whenEmailInUse_shouldFail() {
        UserDTO dto = new UserDTO();
        dto.setUsername("John");
        dto.setEmail("Jane_doe@mail.com"); //already in use
        dto.setPassword("1234@Abcd");

        assertThrows(ProfileException.class, () -> profileService.updateUser(dto));
    }
}
