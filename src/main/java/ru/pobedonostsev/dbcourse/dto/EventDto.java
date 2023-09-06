package ru.pobedonostsev.dbcourse.dto;

import java.sql.Timestamp;

public class EventDto {
    private long id;
    private String name;
    private String date;
    private String hostName;
    private long host;

    public long getId() {
        return id;
    }

    public EventDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDate() {
        return date;
    }

    public EventDto setDate(String date) {
        this.date = date;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public EventDto setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public long getHost() {
        return host;
    }

    public EventDto setHost(long host) {
        this.host = host;
        return this;
    }
}
