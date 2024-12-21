package com.smartjob.userreg.infrastructure.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JWTAuthorizationFilter implements Filter {

    public static final String UNAUTHORIZED_MESSAGE = "{\"message\": \"The token is invalid. Make sure you have entered a valid token in the Authorization header (Bearer Authorization)\"}";

    private ServletContext servletContext;

    /**
     * Filters incoming HTTP requests to validate the JWT token in the Authorization header.
     * This method intercepts the request and checks if the Authorization header contains a valid JWT token.
     * If the token is valid, it allows the request to proceed by calling FilterChain
     * If the token is invalid or absent, it responds with a 401 Unauthorized status and a JSON message indicating the failure.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = AuthorizationHeaderTokenExtractor.getBearerToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (StringUtils.isNotBlank(token)) {
            final ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            final JWTProvider jwtProvider = ctx.getBean(JWTProvider.class);
            if (jwtProvider.validateToken(token)) {
                request.setAttribute(JWTProvider.TOKEN_PARAM, token);
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(UNAUTHORIZED_MESSAGE);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
    }

}
