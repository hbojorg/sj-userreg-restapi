package com.smartjob.userreg.infrastructure;

import com.smartjob.userreg.infrastructure.config.ValidationRegexReader;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final ValidationRegexReader validationRegexReader;

    public EmailValidator(ValidationRegexReader validationRegexReader) {
        this.validationRegexReader = validationRegexReader;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile(validationRegexReader.getEmail());
        return StringUtils.isNotBlank(email) && pattern.matcher(email).matches();
    }
}
