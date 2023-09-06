package ru.pobedonostsev.dbcourse.model;

import ru.pobedonostsev.framework.entity.IEntity;

public class Token implements IEntity<Long> {
    private long id;
    private String refreshToken;
    private long userId;

    @Override
    public Long getPK() {
        return id;
    }

    public Token setId(long id) {
        this.id = id;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Token setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public Token setUserId(long userId) {
        this.userId = userId;
        return this;
    }
}
