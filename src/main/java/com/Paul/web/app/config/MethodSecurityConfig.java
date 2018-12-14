package com.Paul.web.app.config;


import com.Paul.web.app.config.methodSecurity.CustomMethodSecurityExpressionHandler;
import com.Paul.web.app.config.methodSecurity.CustomPermissionEvaluator;
import com.Paul.web.app.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig
        extends GlobalMethodSecurityConfiguration{



    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {

        return customExpressionHandler(null);
    }

    @Bean
    public MethodSecurityExpressionHandler customExpressionHandler(UserService userService) {
        final CustomMethodSecurityExpressionHandler expressionHandler =
                new CustomMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        expressionHandler.setUserService(userService);
        return expressionHandler;

    }
}
