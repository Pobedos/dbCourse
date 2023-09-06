package ru.pobedonostsev.dbcourse.dao.token;

public interface TokenTable {
    String TABLE_NAME = "Tokens";
    String C_ID = "id";
    String C_TOKEN = "refreshToken";
    String C_USER = "user_id";
}
