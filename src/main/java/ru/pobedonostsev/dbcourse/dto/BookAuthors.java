package ru.pobedonostsev.dbcourse.dto;

public class BookAuthors {
    private long id;
    private long instanceId;
    private String name;
    private String userName;
    private String[] authors;
    private String[] authorsId;
    private String authorsString;
    private String genre;
    private String state;
    private String status;
    private long count;
    private long userId;

    public long getId() {
        return id;
    }

    public BookAuthors setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BookAuthors setName(String name) {
        this.name = name;
        return this;
    }

    public String[] getAuthors() {
        return authors;
    }

    public BookAuthors setAuthors(String[] authors) {
        this.authors = authors;
        return this;
    }

    public String[] getAuthorsId() {
        return authorsId;
    }

    public BookAuthors setAuthorsId(String[] authorsId) {
        this.authorsId = authorsId;
        return this;
    }

    public String getAuthorsString() {
        return authorsString;
    }

    public BookAuthors setAuthorsString(String authorsString) {
        this.authorsString = authorsString;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public BookAuthors setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public long getCount() {
        return count;
    }

    public BookAuthors setCount(long count) {
        this.count = count;
        return this;
    }

    public long getInstanceId() {
        return instanceId;
    }

    public BookAuthors setInstanceId(long instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public String getState() {
        return state;
    }

    public BookAuthors setState(String state) {
        this.state = state;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public BookAuthors setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public BookAuthors setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public BookAuthors setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
