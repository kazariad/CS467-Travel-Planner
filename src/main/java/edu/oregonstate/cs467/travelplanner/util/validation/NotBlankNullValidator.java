/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
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
