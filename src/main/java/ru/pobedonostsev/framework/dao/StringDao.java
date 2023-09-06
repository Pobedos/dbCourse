package ru.pobedonostsev.framework.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import ru.pobedonostsev.framework.entity.StringEntity;
import ru.pobedonostsev.framework.exception.DaoException;

public abstract class StringDao<PK, T extends StringEntity<PK>> extends BaseDao<PK, T> {
    protected String createSql = "INSERT INTO " + getTableName() + " (" + getStringColumn() + ") VALUES (:" + getStringColumn() + ")";

    @Override
    public void store(T entity) throws DaoException {
        MapSqlParameterSource params = new MapSqlParameterSource(getStringColumn(), entity.getString());
        try {
            client.update(createSql, params);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    public abstract String getStringColumn();
}
