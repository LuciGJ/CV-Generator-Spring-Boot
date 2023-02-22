package com.luci.cvgenerator.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {

	String message() default "The password must contain at least 8 characters, one lowercase letter, one uppercase letter, one digit and one symbol";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
