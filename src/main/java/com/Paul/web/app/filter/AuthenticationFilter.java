package com.Paul.web.app.filter;

import com.Paul.web.app.security.TokenHandler;
import com.Paul.web.app.security.UserAuthentication;
import com.Paul.web.app.security.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class AuthenticationFilter extends GenericFilterBean {

    private final TokenHandler tokenHandler;
    private final String header;

    public AuthenticationFilter(TokenHandler tokenHandler, @Value("${com.example.demo.security.header}") String header) {
        this.tokenHandler = tokenHandler;
        this.header = header;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(getAuthentication((HttpServletRequest) servletRequest));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String headerValue = request.getHeader(header);
        if (headerValue != null) {
            String[] tokenAndPrefix = headerValue.split(" ");
            if ("Bearer".equals(tokenAndPrefix[0])) {
                UserDetails user = tokenHandler.parseUserFromToken(tokenAndPrefix[1]);
                if (user != null) return new UserAuthentication(user);
            }
        }
        return null;
    }

}
