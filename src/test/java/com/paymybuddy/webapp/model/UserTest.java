package com.paymybuddy.webapp.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1);
        user.setUsername("John");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole("USER");

        assertEquals(1L, user.getId());
        assertEquals("John", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    public void testToStringExcludesPassword() {
        User user = new User();
        user.setId(1);
        user.setUsername("John");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole("USER");

        String userString = user.toString();
        assertFalse(userString.contains("password123"));
    }

    @Test
    public void testUserEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("John");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password123");
        user1.setRole("USER");

        User user2 = new User();
        user2.setId(1);
        user2.setUsername("John");
        user2.setEmail("john.doe@example.com");
        user2.setPassword("password123");
        user2.setRole("USER");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        user2.setId(2);
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Nested
    class ValidationTests {

        private static Validator validator;
        private User user;

        @BeforeAll
        public static void setupValidatorInstance() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        @BeforeEach
        public void setUp() {
            user = new User();
            user.setUsername("John");
            user.setEmail("John_doe@mail.com");
            user.setPassword(null);
            user.setRole("USER");
        }

        @Test
        public void whenValidUser_thenNoViolations() {
            Set<ConstraintViolation<User>> violations = validator.validate(user);

            assertTrue(violations.isEmpty());
        }

        @Test
        public void whenNotNull_thenConstraintViolations() {
            user.setEmail(null);
            user.setUsername(null);
            user.setRole(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user);

            assertEquals(3, violations.stream().filter(violation
                            -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class))
                    .toList().size());
        }

        @Test
        public void whenNotBlank_thenConstraintViolations() {
            user.setEmail(" ");
            user.setUsername(" ");
            user.setRole(" ");
            Set<ConstraintViolation<User>> violations = validator.validate(user);

            assertEquals(3, violations.stream().filter(violation
                            -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class))
                    .toList().size());
        }

        @Test
        public void whenNotEmpty_thenConstraintViolations() {
            user.setEmail("");
            user.setUsername("");
            user.setRole("");
            Set<ConstraintViolation<User>> violations = validator.validate(user);

            assertEquals(3, violations.stream().filter(violation
                            -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotEmpty.class))
                    .toList().size());
        }

        @Test
        public void testValidateEmailSuccess() {
            String emailAddress = "username@domain.com";
            user.setEmail(emailAddress);

            var violations = validator.validateProperty(user, "email");
            assertTrue(violations.isEmpty());
        }


        @Test
        public void testValidateEmail_whenNoAt_shouldFail() {
            user.setEmail("usernamedomain.com");

            var violations = validator.validateProperty(user, "email");
            assertEquals(1, violations.size());
        }
    }
}

