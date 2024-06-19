package com.paymybuddy.webapp.model;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoneyTransferDTOTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenAmountLessThanOne_thenConstraintViolation() {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setReceiverId(123);
        dto.setAmount(0.5);  // less than the min value of 1
        dto.setDescription("Test description");

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Minimal amount is 1")));
    }

    @Test
    public void whenDescriptionIsEmpty_thenConstraintViolation() {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setReceiverId(123);
        dto.setAmount(10);
        dto.setDescription("");

        var violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    public void whenValidDTO_thenNoConstraintViolations() {
        MoneyTransferDTO dto = new MoneyTransferDTO();
        dto.setReceiverId(123);
        dto.setAmount(10);
        dto.setDescription("Valid transaction description");

        var violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
