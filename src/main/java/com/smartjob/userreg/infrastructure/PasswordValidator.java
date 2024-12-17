package com.smartjob.userreg.infrastructure;

import com.smartjob.userreg.infrastructure.config.ValidationRegexReader;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final ValidationRegexReader validationRegexReader;

    public PasswordValidator(ValidationRegexReader validationRegexReader) {
        this.validationRegexReader = validationRegexReader;
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile(validationRegexReader.getPassword());
        return StringUtils.isNotBlank(password) && pattern.matcher(password).matches();
    }
}
