package com.smartjob.userreg.domain.valueobject;

import com.smartjob.userreg.infrastructure.validation.ValidEmail;
import com.smartjob.userreg.infrastructure.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserRegistration(
        @NotBlank(message = "The name must not be empty or null")
        String name,

        @NotBlank(message = "The email must not be empty or null")
        @ValidEmail(message = INVALID_EMAIL_MESSAGE)
        String email,

        @NotBlank(message = "The password password must be empty or null")
        @ValidPassword(message = INVALID_PASSWORD_MESSAGE)
        String password,
        List<PhoneRegistration> phones
) {
        public static final String INVALID_EMAIL_MESSAGE = "The email is invalid. It must not be empty, and it must have a domain or provider type from Chile, for example: name@domain.cl";

        public static final String INVALID_PASSWORD_MESSAGE = "The password is invalid. Ensure that it includes: at least 1 lowercase letter, at least 1 uppercase letter, at least 1 number, and at least 1 special character. The password must be at least 7 characters long and contain no spaces";
}
