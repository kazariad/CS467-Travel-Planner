package edu.oregonstate.cs467.travelplanner.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must contain at least one non-whitespace character. {@code null} values are valid.
 * Accepts {@code CharSequence}.
 */
@Documented
@Constraint(validatedBy = NotBlankNullValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(NotBlankNull.List.class)
public @interface NotBlankNull {
	String message() default "{jakarta.validation.constraints.NotBlank.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Defines several {@code @NotBlankNull} constraints on the same element.
	 *
	 * @see NotBlankNull
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		NotBlankNull[] value();
	}
}
