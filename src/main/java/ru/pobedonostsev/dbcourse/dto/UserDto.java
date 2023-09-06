package ru.pobedonostsev.dbcourse.dto;

public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private String role;
    private String number;

    public long getId() {
        return id;
    }

    public UserDto setId(long id) {
        this.id = id;
        return this;
    }

    public UserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDto setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserDto setRole(String role) {
        this.role = role;
        return this;
    }

    public UserDto setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getNumber() {
        return number;
    }
}
