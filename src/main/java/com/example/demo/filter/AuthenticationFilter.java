package com.example.demo.filter;

import com.example.demo.security.TokenHandler;
import com.example.demo.security.UserAuthentication;
import com.example.demo.security.UserDetails;
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
        String token = request.getHeader(header);
        if (token != null) {
            UserDetails user = tokenHandler.parseUserFromToken(token);
            if (user != null) return new UserAuthentication(user);
        }
        return null;
    }

}
