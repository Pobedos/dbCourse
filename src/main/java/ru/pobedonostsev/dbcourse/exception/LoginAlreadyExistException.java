package ru.pobedonostsev.dbcourse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class LoginAlreadyExistException extends RuntimeException {
    public LoginAlreadyExistException(String s) {
        super(s);
    }
}
