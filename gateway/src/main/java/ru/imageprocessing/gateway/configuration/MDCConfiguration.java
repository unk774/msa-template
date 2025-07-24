package ru.imageprocessing.gateway.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MDCConfiguration extends OncePerRequestFilter {

    public static final String MDC_KEY = "userId";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var securityContext = SecurityContextHolder.getContext();
        String userId = "system";
        if (securityContext != null && securityContext.getAuthentication() != null
                && securityContext.getAuthentication().getName() != null) {
            userId = securityContext.getAuthentication().getName();
        }
        MDC.put(MDC_KEY, userId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }
}
