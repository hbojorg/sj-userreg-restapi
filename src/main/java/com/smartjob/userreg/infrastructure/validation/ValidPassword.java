package com.smartjob.userreg.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordRegexValidator.class)
public @interface ValidPassword {

    String message() default "The password is invalid. Ensure that it includes: at least 1 lowercase letter, at least 1 uppercase letter, at least 1 number, and at least 1 special character. The password must be at least 7 characters long and contain no spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
