package ru.pobedonostsev.framework.dao;

import org.springframework.jdbc.core.RowMapper;

import ru.pobedonostsev.framework.entity.IEntity;
import ru.pobedonostsev.framework.exception.DaoException;

public interface IEntityDao<PK, T extends IEntity<PK>> {
    void store(T entity) throws DaoException;

    T load(PK pk) throws DaoException;

    void update(T entity) throws DaoException;

    void remove(PK pk) throws DaoException;

    String getTableName();

    String getPKColumn();

    RowMapper<T> getMapper();

    boolean exist(PK pk) throws DaoException;
}
