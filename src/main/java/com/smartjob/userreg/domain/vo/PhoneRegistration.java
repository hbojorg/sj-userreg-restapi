package com.smartjob.userreg.domain.vo;

import jakarta.validation.constraints.NotBlank;

public record PhoneRegistration(
        @NotBlank(message = "The number is invalid")
        String number,

        @NotBlank(message = "The city code is invalid")
        String cityCode,

        @NotBlank(message = "The country code is invalid")
        String countryCode
) {

}
