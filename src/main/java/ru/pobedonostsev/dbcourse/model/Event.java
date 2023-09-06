package ru.pobedonostsev.dbcourse.model;

import java.sql.Timestamp;

import ru.pobedonostsev.framework.entity.IEntity;

public class Event implements IEntity<Long> {
    private long id;
    private long host;
    private String name;
    private String date;

    @Override
    public Long getPK() {
        return id;
    }

    public Event setId(long id) {
        this.id = id;
        return this;
    }

    public long getHost() {
        return host;
    }

    public Event setHost(long host) {
        this.host = host;
        return this;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Event setDate(String date) {
        this.date = date;
        return this;
    }
}
