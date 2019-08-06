package com.vova.purchaseservice.security;

import com.vova.purchaseservice.ex.TooManyAttemptException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BasicAuth extends BasicAuthenticationEntryPoint {


    private final Logger logger = Logger.getLogger("basic-auth-handler");

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException, ServletException {
        logger.log(Level.INFO, "Try to login from ip " + request.getRemoteAddr(), authEx);
        if (!TooManyAttemptException.isTooManyAuthException(authEx)) {
            logger.log(Level.INFO, "Ip not blocked, send need authorize header");
            response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            logger.log(Level.INFO, "Ip blocked, send 403");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        PrintWriter writer = response.getWriter();
        String attribute = (String) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        String message = StringUtils.defaultIfBlank(attribute, authEx.getMessage());
        writer.println("HTTP Status " + response.getStatus() + " - " + message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("PurchaseService");
        super.afterPropertiesSet();
    }
}
