package com.example.footballapi.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

//@Component
@Order(1)
public class LoggingAndTracingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoggingAndTracingFilter.class);
    private static final String CORRELATION_ID_HEADER = "X-Request-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String correlationId = httpReq.getHeader(CORRELATION_ID_HEADER);
        if (!StringUtils.hasText(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
        httpRes.setHeader(CORRELATION_ID_HEADER, correlationId);

        long startTime = System.currentTimeMillis();

        try {
            if (httpReq.getRequestURI().startsWith("/api/")) {
                log.info("Request started: {} {}", httpReq.getMethod(), httpReq.getRequestURI());
            }

            chain.doFilter(request, response);

        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (httpReq.getRequestURI().startsWith("/api/")) {
                log.info("Request finished: {} {} -> status={} in {}ms",
                        httpReq.getMethod(), httpReq.getRequestURI(), httpRes.getStatus(), duration);
            }

            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }
}