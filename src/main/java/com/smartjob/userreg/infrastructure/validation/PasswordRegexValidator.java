package com.smartjob.userreg.infrastructure.validation;

import com.smartjob.userreg.shared.config.ValidationRegexPropertiesReader;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordRegexValidator implements ConstraintValidator<ValidPassword, String> {

    private final ValidationRegexPropertiesReader validationRegexPropertiesReader;

    public PasswordRegexValidator(ValidationRegexPropertiesReader validationRegexPropertiesReader) {
        this.validationRegexPropertiesReader = validationRegexPropertiesReader;
    }

    /*
     * Validates a password based on a password regular expression, this regular expression is read from a properties file
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile(validationRegexPropertiesReader.getPassword());
        return StringUtils.isNotBlank(password) && pattern.matcher(password).matches();
    }
}
