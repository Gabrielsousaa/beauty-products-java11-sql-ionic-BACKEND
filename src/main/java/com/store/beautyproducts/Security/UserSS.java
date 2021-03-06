package com.store.beautyproducts.Security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.store.beautyproducts.domain.enums.Profile;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserSS implements UserDetails {

    private Integer userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    
    public UserSS(){

    }
    

    public UserSS(Integer userId, String email, String password, Set<Profile> profiles) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = profiles.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      
        return authorities;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
      
        return password;
    }

    @Override
    public String getUsername() {
        
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(Profile profile) {
    return getAuthorities().contains(new SimpleGrantedAuthority(profile.getDescricao()));
    }
}
