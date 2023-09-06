package ru.pobedonostsev.framework.service;

import ru.pobedonostsev.framework.entity.IEntity;
import ru.pobedonostsev.framework.exception.DaoException;

public interface IService<PK, T extends IEntity<PK>> {
    void delete(PK pk) throws DaoException;
    T get(PK pk) throws DaoException;
}
