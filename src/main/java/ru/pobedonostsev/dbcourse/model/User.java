package ru.pobedonostsev.dbcourse.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.gson.annotations.Expose;

import ru.pobedonostsev.framework.entity.IEntity;

public class User implements IEntity<Long>, UserDetails {
    private long id;
    private String firstName;
    private String lastName;
    private String login;
    @Expose(serialize = false)
    private String password;
    private long roleId;
    private String role;
    private String number;
    private boolean request;

    @Override
    public Long getPK() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public long getRoleId() {
        return roleId;
    }

    public String getNumber() {
        return number;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setRoleId(long roleId) {
        this.roleId = roleId;
        return this;
    }

    public User setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    public boolean isRequest() {
        return request;
    }

    public User setRequest(boolean request) {
        this.request = request;
        return this;
    }
}
