package ru.pobedonostsev.dbcourse.dto;

public class UserDtoWithoutPassword {
    private long id;
    private String firstName;
    private String lastName;
    private String login;
    private String role;
    private String number;

    public long getId() {
        return id;
    }

    public UserDtoWithoutPassword setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDtoWithoutPassword setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDtoWithoutPassword setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UserDtoWithoutPassword setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getRole() {
        return role;
    }

    public UserDtoWithoutPassword setRole(String role) {
        this.role = role;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public UserDtoWithoutPassword setNumber(String number) {
        this.number = number;
        return this;
    }
}
