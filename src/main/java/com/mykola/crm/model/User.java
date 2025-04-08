package com.mykola.crm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private short yearsOld;

    public User(String username, String password, String email, byte yearsOld) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.yearsOld = yearsOld;
    }

    @Override//UserDetails
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override//UserDetails
    public String getPassword() {
        return this.password;
    }

    @Override//UserDetails
    public String getUsername() {
        return this.username;
    }

    @Override//UserDetails
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override//UserDetails
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override//UserDetails
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override//UserDetails
    public boolean isEnabled() {
        return true;
    }
}
