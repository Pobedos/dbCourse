package ru.pobedonostsev.dbcourse.dto;

public class AuthDto {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public AuthDto setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
