package com.ticketbox.auth_service.validations.validators;

import com.ticketbox.auth_service.validations.NotAlphaNumeric;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class NotAlphaNumericValidator implements ConstraintValidator<NotAlphaNumeric, String> {
    @Override
    public void initialize(NotAlphaNumeric constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;
        return value.matches("^[a-zA-Z]+$");
    }
}
