package com.smartjob.userreg.domain.valueobject;

import com.smartjob.userreg.infrastructure.validation.ValidEmail;
import com.smartjob.userreg.infrastructure.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserRegistration(
        @NotBlank(message = "The name must not be empty or null")
        String name,

        @ValidEmail
        String email,

        @ValidPassword
        String password,
        List<PhoneRegistration> phones
) {

}
