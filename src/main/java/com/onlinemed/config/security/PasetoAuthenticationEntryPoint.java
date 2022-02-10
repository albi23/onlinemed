package com.onlinemed.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class will reject every unauthenticated request
 */
@Component
public class PasetoAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authEx) throws IOException {
        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
