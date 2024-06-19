package com.paymybuddy.webapp.it;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.IUserService;
import com.paymybuddy.webapp.service.SignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class SignUpServiceIT {

    @Autowired
    private UserRepository userRepository;
    @Mock
    private IUserService userService;
    @Mock
    private BCryptPasswordEncoder encoder;
    @InjectMocks
    private SignUpService signUpService;

    @Test
    @DisplayName("Given saving user fails, when sign up new user, then throw exception and rollback")
    public void givenTransactionalFails_whenSignUp_thenDoNotCreateNewUser() {
        //GIVEN
        UserDTO form = new UserDTO();
        form.setUsername("New user");
        form.setEmail("new_user@mail.com");
        form.setPassword("1234@Abcd");
        when(userService.saveUser(any(User.class))).thenThrow(new RuntimeException());

        //WHEN
        assertThrows(RuntimeException.class, () -> signUpService.signUp(form));

        //THEN
        verify(userService, times(1)).saveUser(any(User.class));
        assertFalse(userRepository.findByEmail("new_user@mail.com").isPresent());
    }
}
