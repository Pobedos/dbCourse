package ru.pobedonostsev.framework.service;

import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.entity.IEntity;
import ru.pobedonostsev.framework.exception.DaoException;
import ru.pobedonostsev.framework.exception.EntityNotFoundException;

public abstract class BaseService<PK, T extends IEntity<PK>> implements IService<PK, T> {
    public T get(PK pk) throws DaoException {
        T entity = getDao().load(pk);
        if (entity == null) {
            throw new EntityNotFoundException("Entity not found");
        }
        return entity;
    }

    public void delete(PK pk) throws DaoException {
        getDao().remove(pk);
    }

    protected abstract BaseDao<PK, T> getDao();
}
