package edu.oregonstate.cs467.travelplanner.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Custom annotation to enforce that startDate <= endDate.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidDateRange {
    String message() default "Start date must not be after end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}