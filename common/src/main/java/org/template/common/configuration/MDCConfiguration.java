package org.template.common.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class MDCConfiguration extends OncePerRequestFilter {

    public static final String MDC_USER = "userId";
    public static final String MDC_USER_ROLES = "userRoles";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var securityContext = SecurityContextHolder.getContext();
        String userId = "system";
        List<String> roles = Collections.emptyList();
        if (securityContext != null && securityContext.getAuthentication() != null
                && securityContext.getAuthentication().getName() != null) {
            userId = securityContext.getAuthentication().getName();
            roles = securityContext.getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        }
        MDC.put(MDC_USER, userId);
        MDC.put(MDC_USER_ROLES, String.join(",", roles));
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER);
            MDC.remove(MDC_USER_ROLES);
        }
    }
}
