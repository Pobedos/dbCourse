package ru.pobedonostsev.dbcourse.model;

import ru.pobedonostsev.framework.entity.StringEntity;

public class Author implements StringEntity<Long> {

    private long id;
    private String name;

    @Override
    public Long getPK() {
        return id;
    }

    @Override
    public String getString() {
        return name;
    }

    public Author setId(long id) {
        this.id = id;
        return this;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }
}
