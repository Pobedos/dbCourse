package ru.pobedonostsev.dbcourse.dto;

public class AuthorDto {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public AuthorDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AuthorDto setName(String name) {
        this.name = name;
        return this;
    }
}
