package com.paymybuddy.webapp.model;

import com.paymybuddy.webapp.dto.RegistrationForm;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationFormTest {

    @Test
    public void testSettersAndGetters() {
        RegistrationForm form = new RegistrationForm();
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
        RegistrationForm form = new RegistrationForm("jane.doe@example.com", "secret", "Jane");

        // Assert toString does not include password for security reasons
        assertThat(form.toString()).doesNotContain("secret");
        assertThat(form.toString()).contains("Jane", "jane.doe@example.com");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Create two identical instances
        RegistrationForm form1 = new RegistrationForm("jane.doe@example.com", "secret", "Jane");
        RegistrationForm form2 = new RegistrationForm("jane.doe@example.com", "secret", "Jane");

        // Assert these two are considered equal and have the same hash code
        assertThat(form1).isEqualTo(form2);
        assertThat(form1.hashCode()).isEqualTo(form2.hashCode());

        // Change one of them
        form1.setUsername("Smith");

        // Assert this is no longer equal to the other two
        assertThat(form1).isNotEqualTo(form2);
        assertThat(form1.hashCode()).isNotEqualTo(form2.hashCode());
    }
}


