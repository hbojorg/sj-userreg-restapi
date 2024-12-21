package com.smartjob.userreg.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    /**
     * Configures and registers a custom OpenAPI specification for API documentation.
     * This method sets up an OpenAPI instance with security configuration for bearer token
     * authorization. It defines the "Bearer Authorization" security scheme, which uses
     * JWT tokens for authentication. The security scheme is applied globally to the API
     * by adding it to the OpenAPI components and security requirements.
     *
     * @return a configured OpenAPI object with security settings for JWT bearer tokens.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authorization";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));

    }

}
