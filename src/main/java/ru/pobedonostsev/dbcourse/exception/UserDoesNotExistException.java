package ru.pobedonostsev.dbcourse.exception;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String s) {
        super(s);
    }
}
