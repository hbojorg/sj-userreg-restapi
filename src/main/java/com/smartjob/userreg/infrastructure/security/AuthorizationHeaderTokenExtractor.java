package com.smartjob.userreg.infrastructure.security;

import org.apache.commons.lang3.StringUtils;

public class AuthorizationHeaderTokenExtractor {

    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * Extracts the Bearer token from the Authorization header.
     * @param authorizationHeader the authorization header
     * @return the Bearer token if present in the Authorization header, otherwise null
     */
    public static String getBearerToken(String authorizationHeader) {
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}
