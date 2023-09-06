package ru.pobedonostsev.dbcourse.dao.token;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import ru.pobedonostsev.dbcourse.model.Token;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Repository
public class TokenDao extends BaseDao<Long, Token> implements TokenTable {
    private static final String INSERT_SQL =
    """
        INSERT INTO tokens(user_id, refreshToken) VALUES (:user_id, :refreshToken)
        ON CONFLICT (user_id) DO UPDATE
        SET refreshToken=:refreshToken;
    """;

    @Override
    public void store(Token entity) throws DaoException {
        MapSqlParameterSource map = new MapSqlParameterSource(C_USER, entity.getUserId());
        map.addValue(C_TOKEN, entity.getRefreshToken());
        client.update(INSERT_SQL, map);
    }

    @Override
    public void update(Token entity) throws DaoException {
        store(entity);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getPKColumn() {
        return C_ID;
    }

    @Override
    public RowMapper<Token> getMapper() {
        return (rs, rowNum) -> new Token()
            .setId(rs.getLong(C_ID))
            .setUserId(rs.getLong(C_USER))
            .setRefreshToken(rs.getString(C_TOKEN));
    }

    private static final String GET_REFRESH =
        """
            SELECT id, refreshToken, user_id FROM Tokens
            WHERE refreshToken=:refreshToken
        """;

    public Token getByRefresh(String refresh) {
        List<Token> result = client.query(GET_REFRESH, new MapSqlParameterSource(C_TOKEN, refresh), getMapper());
        return result.isEmpty() ? null : result.get(0);
    }

    private static final String REMOVE =
        """
            DELETE FROM Tokens WHERE refreshToken=:refreshToken;
        """;

    public void removeByRefreshToken(String refreshToken) {
        client.update(REMOVE, new MapSqlParameterSource(C_TOKEN, refreshToken));
    }
}
