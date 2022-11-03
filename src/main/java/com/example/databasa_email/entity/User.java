package com.example.databasa_email.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp //creat bogan payt timeii yaratiladiw
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updateAt;
    @ManyToMany
    private Set<Role> roles;
    private String EmailCode;

    private boolean accountNonExpired = true; //user amal qilish mudati
    private boolean accountNonLocked = true; //user bloklanmaganllligi
    private boolean credentialsNonExpired = true; //
    private boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
//accountni amal qiilish mudati
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }
//bloklanganlik holati
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }
//ishonchlilik mudati tugaganligini qaytaradi
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }
//aktiv aktivmasligini qaytaradi
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
