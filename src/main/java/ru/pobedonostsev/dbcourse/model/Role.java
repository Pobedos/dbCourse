package ru.pobedonostsev.dbcourse.model;

import ru.pobedonostsev.framework.entity.StringEntity;

public class Role implements StringEntity<Long> {
    private long id;
    private String name;

    public Role() {
    }

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getPK() {
        return id;
    }

    @Override
    public String getString() {
        return name;
    }
}
