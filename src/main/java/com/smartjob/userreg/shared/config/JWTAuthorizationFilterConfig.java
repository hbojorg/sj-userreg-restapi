package com.smartjob.userreg.shared.config;

import com.smartjob.userreg.infrastructure.security.JWTAuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTAuthorizationFilterConfig {

    /**
     * Registers a filter to validate JWT tokens for requests to specific API endpoints.
     * This method creates and configures a FilterRegistrationBean for the JWTAuthorizationFilter.
     * The filter is applied to all incoming requests with URLs matching the pattern "/api/*".
     * It intercepts requests to verify the presence and validity of the JWT token in the Authorization header.
     *
     * @return a FilterRegistrationBean that registers the JWTAuthorizationFilter for the specified URL pattern "/api/*".
     */
    @Bean
    public FilterRegistrationBean<JWTAuthorizationFilter> tokenValidationFilter() {
        FilterRegistrationBean<JWTAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JWTAuthorizationFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

}
