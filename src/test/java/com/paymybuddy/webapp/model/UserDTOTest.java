package com.paymybuddy.webapp.model;

import com.paymybuddy.webapp.dto.UserDTO;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserDTOTest {

    @Test
    public void testSettersAndGetters() {
        UserDTO form = new UserDTO();
        form.setPassword("password");
        form.setUsername("John");
        form.setEmail("john_doe@mail.com");

        assertEquals("password", form.getPassword());
        assertEquals("John", form.getUsername());
        assertEquals("john_doe@mail.com", form.getEmail());
    }

    @Test
    public void testToStringMethod() {
        // Create an instance with specific attributes
        UserDTO form = new UserDTO("jane.doe@example.com",
                "secret", "Jane");

        // Assert toString does not include password for security reasons
        assertThat(form.toString()).doesNotContain("secret");
        assertThat(form.toString()).contains("Jane", "jane.doe@example.com");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Create two identical instances
        UserDTO form1 = new UserDTO("jane.doe@example.com", "secret", "Jane");
        UserDTO form2 = new UserDTO("jane.doe@example.com", "secret", "Jane");

        // Assert these two are considered equal and have the same hash code
        assertThat(form1).isEqualTo(form2);
        assertThat(form1.hashCode()).isEqualTo(form2.hashCode());

        // Change one of them
        form1.setUsername("Smith");

        // Assert this is no longer equal to the other two
        assertThat(form1).isNotEqualTo(form2);
        assertThat(form1.hashCode()).isNotEqualTo(form2.hashCode());
    }

    @Nested
    public class ValidateTests {

        private UserDTO form;
        private static Validator validator;

        @BeforeAll
        public static void setupValidatorInstance() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        @BeforeEach
        public void setUp() {
            //Set up with valid fields
            form = new UserDTO();
            form.setUsername("Jane_Doe");
            form.setPassword("ValidPass@1");
            form.setEmail("valid@mail.com");
        }

        @Test
        public void testValidateEmailSuccess() {
            String emailAddress = "username@domain.com";
            form.setEmail(emailAddress);

            var violations = validator.validateProperty(form, "email");
            assertTrue(violations.isEmpty());
        }

        @Test
        public void testValidateEmail_whenNoDot_shouldFail() {
            form.setEmail("username@domaincom");

            var violations = validator.validateProperty(form, "email");
            assertFalse(violations.isEmpty());
        }

        @Test
        public void testValidateEmail_whenNoAt_shouldFail() {
            form.setEmail("usernamedomain.com");

            var violations = validator.validateProperty(form, "email");
            assertFalse(violations.isEmpty());
        }

        @Test
        public void givenStringPassword_whenUsingRegulaExpressions_thenCheckIfPasswordValid() {
            form.setPassword("AbCD123@");

            var violations = validator.validateProperty(form, "password");
            assertTrue(violations.isEmpty());
        }

        @Test
        public void givenPasswordContainsNoDigits_whenUsingRegulaExpressions_thenPasswordIsInValid() {
            form.setPassword("AbCDEFGH@");

            var violations = validator.validateProperty(form, "password");
            assertFalse(violations.isEmpty());
        }


        @Test
        public void whenNotEmptyName_thenNoConstraintViolations() {
            form.setUsername("John");
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(form);

            assertThat(violations.size()).isEqualTo(0);
        }

        @Test
        public void whenEmptyName_thenConstraintViolation() {
            form.setUsername("");
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(form);

            assertTrue(violations.stream().anyMatch(violation
                    -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotEmpty.class)));
        }

        @Test
        public void whenNullName_thenConstraintViolation() {
            form.setUsername(null);
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(form);

            assertTrue(violations.stream().anyMatch(violation
                    -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class)));
        }

        @Test
        public void whenBlankName_thenConstraintViolation() {
            form.setUsername(" ");
            Set<ConstraintViolation<UserDTO>> violations = validator.validate(form);

            assertTrue(violations.stream().anyMatch(violation
                    -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class)));
        }
    }
}


