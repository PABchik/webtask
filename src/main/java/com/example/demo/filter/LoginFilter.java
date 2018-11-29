package com.example.demo.filter;

import com.example.demo.security.TokenHandler;
import com.example.demo.entity.User;
import com.example.demo.security.UserAuthentication;
import com.example.demo.security.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    private final TokenHandler tokenHandler;
    private final Long expires;
    private final String header;

    public LoginFilter(String defaultFilterProcessesUrl,
                       UserDetailsService userDetailsService,
                       AuthenticationManager authenticationManager, TokenHandler tokenHandler, Long expires, String header) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, "POST"));
        this.userDetailsService = userDetailsService;
        this.tokenHandler = tokenHandler;
        this.expires = expires;
        this.header = header;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        User user;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            throw new BadCredentialsException("Bad credentials");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetails userWithAuthorities = (UserDetails) userDetailsService.loadUserByUsername(authResult.getName());
        UserAuthentication userAuthentication = new UserAuthentication(userWithAuthorities);
        addAuthentication(response, userAuthentication);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }

    private void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        UserDetails user = authentication.getDetails();
        user.setExpires(new Date().getTime() + expires);
        response.addHeader(header, tokenHandler.createTokenForUser(user));
    }

}
