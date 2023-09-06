package ru.pobedonostsev.dbcourse.model;

import ru.pobedonostsev.framework.entity.StringEntity;

public class Genre implements StringEntity<Long> {

    private Long id;
    private String name;

    @Override
    public Long getPK() {
        return id;
    }

    @Override
    public String getString() {
        return name;
    }

    public Genre setId(Long id) {
        this.id = id;
        return this;
    }

    public Genre setName(String name) {
        this.name = name;
        return this;
    }
}
