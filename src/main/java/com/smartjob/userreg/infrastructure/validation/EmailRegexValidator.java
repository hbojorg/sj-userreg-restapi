package com.smartjob.userreg.infrastructure.validation;

import com.smartjob.userreg.shared.config.ValidationRegexPropertiesReader;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EmailRegexValidator implements ConstraintValidator<ValidEmail, String> {

    private final ValidationRegexPropertiesReader validationRegexPropertiesReader;

    public EmailRegexValidator(ValidationRegexPropertiesReader validationRegexPropertiesReader) {
        this.validationRegexPropertiesReader = validationRegexPropertiesReader;
    }

    /*
      * Validates an email based on an email regular expression, this regular expression is read from a properties file
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile(validationRegexPropertiesReader.getEmail());
        return StringUtils.isNotBlank(email) && pattern.matcher(email).matches();
    }
}
