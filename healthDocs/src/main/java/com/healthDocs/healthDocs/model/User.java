package com.healthDocs.healthDocs.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
@Data
@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String EMBG;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(value= EnumType.STRING)
    private Role role;
    private Boolean insurance;

    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=true;
    public User() {
    }
    public Role getRole() {
        return role;
    }


    public User(String EMBG, String username, String password, String firstName, String lastName, Role role, Boolean insurance) {

        this.EMBG = EMBG;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role=role;
        this.insurance=insurance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEMBG() {
        return EMBG;
    }

    public void setEMBG(String EMBG) {
        this.EMBG = EMBG;
    }

    public String getUsername() {
        return username;
    }

    public String getEmbgAndFullName() {
        return this.EMBG + " " + this.firstName + " " + this.lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
	public Boolean getInsurance() {
		return insurance;
	}
	public void setInsurance(Boolean insurance) {
		this.insurance = insurance;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
