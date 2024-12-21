package com.smartjob.userreg.domain.valueobject;

import jakarta.validation.constraints.NotBlank;

public record PhoneRegistration(
        @NotBlank(message = "The phone number must not be empty or null")
        String number,

        @NotBlank(message = "The city code must not be empty or null")
        String cityCode,

        @NotBlank(message = "The country code must not be empty or null")
        String countryCode
) {

}
