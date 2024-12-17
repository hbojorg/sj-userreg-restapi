package com.smartjob.userreg.infrastructure.config;

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
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JWTAuthorizationFilter implements Filter {

    public static final String BEARER_PREFIX = "Bearer ";

    private ServletContext servletContext;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token;
        if (StringUtils.isNotBlank(header) && header.startsWith(BEARER_PREFIX)) {
            token = header.substring(7);
            final ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            final JWTProvider jwtProvider = ctx.getBean(JWTProvider.class);
            if (jwtProvider.validateToken(token)) {
                request.setAttribute(JWTProvider.TOKEN_PARAM, token);
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Invalid token\"}");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
    }

}
