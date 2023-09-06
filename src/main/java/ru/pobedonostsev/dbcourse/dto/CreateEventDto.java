package ru.pobedonostsev.dbcourse.dto;

import java.sql.Timestamp;
import java.util.Arrays;

public class CreateEventDto {
    private long id;
    private String name;
    private long host;
    private Long[] books;
    private String time;

    public String getName() {
        return name;
    }

    public CreateEventDto setName(String name) {
        this.name = name;
        return this;
    }

    public long getHost() {
        return host;
    }

    public CreateEventDto setHost(long host) {
        this.host = host;
        return this;
    }

    public Long[] getBooks() {
        return books;
    }

    public CreateEventDto setBooks(Long[] books) {
        this.books = books;
        return this;
    }

    public String getTime() {
        return time;
    }

    public CreateEventDto setTime(String time) {
        this.time = time;
        return this;
    }

    public long getId() {
        return id;
    }

    public CreateEventDto setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "CreateEventDto{" +
            "name='" + name + '\'' +
            ", host=" + host +
            ", books=" + Arrays.toString(books) +
            ", time=" + time +
            '}';
    }
}
