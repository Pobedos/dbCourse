package ru.pobedonostsev.dbcourse.dto;

import ru.pobedonostsev.dbcourse.model.User;

public class AuthResponse {
    private String refreshToken;
    private String accessToken;
    private User user;

    public String getRefreshToken() {
        return refreshToken;
    }

    public AuthResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public AuthResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public User getUser() {
        return user;
    }

    public AuthResponse setUser(User user) {
        this.user = user;
        return this;
    }
}
