package com.clicks.fulafiaresultcheckingverificationsystem.model.auth;

import com.clicks.fulafiaresultcheckingverificationsystem.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
public class AuthenticatedUser implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(user.getUserRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getIsLocked();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getIsLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getIsLocked();
    }

    @Override
    public boolean isEnabled() {
        return user.getIsLocked();
    }
}
