package com.smartjob.userreg.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "validation.regex")
@Getter
@Setter
public class ValidationRegexReader {

    private String email;

    private String password;

}
