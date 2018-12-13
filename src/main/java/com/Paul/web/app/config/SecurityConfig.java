package com.Paul.web.app.config;

import com.Paul.web.app.filter.AuthenticationFilter;
import com.Paul.web.app.filter.LoginFilter;
import com.Paul.web.app.security.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final AuthenticationFilter authenticationFilter;
    private final UserDetailsService userDetailsService;
    private final TokenHandler tokenHandler;
    private final Long expires;
    private final String header;

    public SecurityConfig(AuthenticationFilter authenticationFilter, UserDetailsService userDetailsService,
                          TokenHandler tokenHandler,
                          @Value("${com.example.demo.security.expires}") Long expires,
                          @Value("${com.example.demo.security.header}") String header) {
        super(true);
        this.authenticationFilter = authenticationFilter;
        this.userDetailsService = userDetailsService;
        this.tokenHandler = tokenHandler;
        this.expires = expires;
        this.header = header;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .exceptionHandling().and()
                .anonymous().and()
                .headers().cacheControl().and().and()

                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers("/**").authenticated().and()


                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter("/login", userDetailsService, authenticationManager(), tokenHandler, expires, header),
                        UsernamePasswordAuthenticationFilter.class);
    }



}
