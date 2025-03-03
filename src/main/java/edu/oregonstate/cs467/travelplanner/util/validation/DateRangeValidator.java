package edu.oregonstate.cs467.travelplanner.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;
import java.time.LocalDate;

/**
 * Enforces the rule defined by @ValidDateRange.
 * Ensures startDate <= endDate.
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
       try {
           Method getStartDate = obj.getClass().getMethod("getStartDate");
           Method getEndDate = obj.getClass().getMethod("getEndDate");

           LocalDate startDate = (LocalDate) getStartDate.invoke(obj);
           LocalDate endDate = (LocalDate) getEndDate.invoke(obj);
           return !startDate.isAfter(endDate);
       } catch (Exception e) {
           return false;
       }
    }
}