package ru.pobedonostsev.dbcourse.dto;

public class DoubleIdDto {
    private long id;
    private long userId;

    public long getId() {
        return id;
    }

    public DoubleIdDto setId(long id) {
        this.id = id;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public DoubleIdDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }
}
