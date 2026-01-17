package com.example.footballapi.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class PerformanceWarningFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(PerformanceWarningFilter.class);
    private static final long WARNING_THRESHOLD_MS = 20;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        long start = System.currentTimeMillis();

        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - start;
        if (duration > WARNING_THRESHOLD_MS && httpReq.getRequestURI().startsWith("/api/")) {
            log.warn("Slow request detected: {} {} took {}ms",
                    httpReq.getMethod(), httpReq.getRequestURI(), duration);
        }
    }
}
