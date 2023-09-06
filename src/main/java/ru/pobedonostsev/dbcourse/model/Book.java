package ru.pobedonostsev.dbcourse.model;

import ru.pobedonostsev.framework.entity.IEntity;

public class Book implements IEntity<Long> {

    private long id;
    private long genreId;
    private String name;

    @Override
    public Long getPK() {
        return id;
    }

    public long getGenreId() {
        return genreId;
    }

    public String getName() {
        return name;
    }

    public Book setPK(long id) {
        this.id = id;
        return this;
    }

    public Book setGenreId(long genreId) {
        this.genreId = genreId;
        return this;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }
}
