package com.Paul.web.app.security;

import com.Paul.web.app.entity.Role;
import com.Paul.web.app.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.beans.Transient;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetails extends User implements org.springframework.security.core.userdetails.UserDetails {

    private User user;
    private Long expires;

    public UserDetails(User user) {
        this.user = user;
    }

    private UserDetails() {
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getUserRoles().stream()
                .map(UserGrantedAuthority::fromRole)
                .collect(Collectors.toList());
    }

    @Override
    @Transient
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    @Transient
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    private static class UserGrantedAuthority implements GrantedAuthority {

        private final String role;

        private UserGrantedAuthority(String role) {
            this.role = role;
        }

        @Override
        public String getAuthority() {
            return role;
        }

        private static UserGrantedAuthority fromRole(Role role) {
            return new UserGrantedAuthority(role.getName());
        }

    }

}
