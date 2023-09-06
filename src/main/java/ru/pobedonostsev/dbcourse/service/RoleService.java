package ru.pobedonostsev.dbcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.pobedonostsev.dbcourse.dao.role.RoleDao;
import ru.pobedonostsev.dbcourse.model.Role;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;
import ru.pobedonostsev.framework.service.BaseService;

@Service
public class RoleService extends BaseService<Long, Role> {
    @Autowired
    private RoleDao roleDao;

    public void create(String name) {
        Role role = new Role();
        role.setName(name);
        try {
            roleDao.store(role);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected BaseDao<Long, Role> getDao() {
        return roleDao;
    }

    public void create(Role role) throws DaoException {
        roleDao.store(role);
    }

    public Role getByName(String role) {
        return roleDao.getByName(role);
    }
}
