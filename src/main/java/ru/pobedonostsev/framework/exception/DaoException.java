package ru.pobedonostsev.framework.exception;

public class DaoException extends Exception {
    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
