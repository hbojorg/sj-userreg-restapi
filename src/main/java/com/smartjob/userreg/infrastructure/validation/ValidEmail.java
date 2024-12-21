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
@Constraint(validatedBy = EmailRegexValidator.class)
public @interface ValidEmail {

    String message() default "The email is invalid. It must not be empty, and it must have a domain or provider type from Chile, for example: name@domain.cl";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
