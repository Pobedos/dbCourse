package ru.pobedonostsev.framework.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.pobedonostsev.framework.entity.IEntity;
import ru.pobedonostsev.framework.exception.DaoException;

public abstract class BaseDao<PK, T extends IEntity<PK>> implements IEntityDao<PK, T> {
    @Autowired
    protected NamedParameterJdbcTemplate client;

    @Override
    public T load(PK pk) throws DaoException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(getTableName());
        sb.append(" WHERE ");
        sb.append(getPKColumn());
        sb.append("=:pk");
        String sql = sb.toString();
        try {
            List<T> result = client.query(sql, new MapSqlParameterSource("pk", pk), getMapper());
            return result.isEmpty() ? null : result.get(0);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void remove(PK pk) throws DaoException {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(getTableName());
        sb.append(" WHERE ");
        sb.append(getPKColumn());
        sb.append("=:pk");
        String sql = sb.toString();
        try {
            client.update(sql, new MapSqlParameterSource("pk", pk));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean exist(PK pk) throws DaoException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT 1 FROM ");
        sb.append(getTableName());
        sb.append(" WHERE ");
        sb.append(getPKColumn());
        sb.append("=:pk");
        String sql = sb.toString();
        try {
            List<Boolean> result = client.query(sql, new MapSqlParameterSource("pk", pk), (rs, rowNum) -> rs.getBoolean(0));
            return !result.isEmpty();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
