package com.smartjob.userreg.domain.vo;

import com.smartjob.userreg.infrastructure.ValidEmail;
import com.smartjob.userreg.infrastructure.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserRegistration(
        @NotBlank(message = "The name is not valid")
        String name,

        @ValidEmail
        String email,

        @ValidPassword
        String password,
        List<PhoneRegistration> phones
) {

}
