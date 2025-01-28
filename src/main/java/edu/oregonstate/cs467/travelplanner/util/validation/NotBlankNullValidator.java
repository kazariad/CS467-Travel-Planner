package edu.oregonstate.cs467.travelplanner.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankNullValidator implements ConstraintValidator<NotBlankNull, CharSequence> {
	@Override
	public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
		if (charSequence == null) return true;
		return charSequence.toString().trim().length() > 0;
	}
}
