package ru.pobedonostsev.dbcourse.dto;

public class ReviewDto {
    private long id;
    private long userId;
    private long bookId;
    private String username;
    private String text;

    public long getId() {
        return id;
    }

    public ReviewDto setId(long id) {
        this.id = id;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public ReviewDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ReviewDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getText() {
        return text;
    }

    public ReviewDto setText(String text) {
        this.text = text;
        return this;
    }

    public long getBookId() {
        return bookId;
    }

    public ReviewDto setBookId(long bookId) {
        this.bookId = bookId;
        return this;
    }
}
