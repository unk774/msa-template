package ru.imageprocessing.common.configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import static ru.imageprocessing.common.configuration.MDCConfiguration.MDC_USER;
import static ru.imageprocessing.common.configuration.MDCConfiguration.MDC_USER_ROLES;

@Configuration
public class RequestLoggingConfig {

    @Slf4j
    public static class RequestLoggingFilter extends AbstractRequestLoggingFilter {

        protected boolean shouldLog(HttpServletRequest request) {
            return true;
        }

        @Override
        protected void beforeRequest(HttpServletRequest request, String message) {
            log.info("user={}; roles={}; message={}", MDC.get(MDC_USER), MDC.get(MDC_USER_ROLES), message);
        }

        @Override
        protected void afterRequest(HttpServletRequest request, String message) {
            log.debug("user={}; roles={}; message={}", MDC.get(MDC_USER), MDC.get(MDC_USER_ROLES), message);
        }
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        RequestLoggingFilter filter
                = new RequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);

        registrationBean.setFilter(filter);
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}