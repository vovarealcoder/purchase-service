package com.vova.purchaseservice.security;

import com.vova.purchaseservice.ex.TooManyAttemptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private MessageSource messages;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = messages.getMessage("message.badCredentials", null, request.getLocale());
        if (TooManyAttemptException.isTooManyAuthException(exception)) {
            errorMessage = messages.getMessage("auth.message.blocked", null, request.getLocale());
        }
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }

    @Autowired
    @Qualifier("purchaseMessageSource")
    public void setMessages(MessageSource messagesSource) {
        this.messages = messagesSource;
    }
}
