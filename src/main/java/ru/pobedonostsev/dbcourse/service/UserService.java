package ru.pobedonostsev.dbcourse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ru.pobedonostsev.dbcourse.dao.user.UserDao;
import ru.pobedonostsev.dbcourse.dto.IdDto;
import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.dto.UserDtoWithoutPassword;
import ru.pobedonostsev.dbcourse.model.Role;
import ru.pobedonostsev.dbcourse.model.User;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;
import ru.pobedonostsev.framework.service.BaseService;

@Service
public class UserService extends BaseService<Long, User> {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Override
    protected BaseDao<Long, User> getDao() {
        return userDao;
    }

    public void create(UserDto userDto) throws DaoException {
        Role role = roleService.getByName(userDto.getRole());
        if (role == null) {
            System.err.println("role is null");
            return;
        }
        userDao.store(new User()
            .setFirstName(userDto.getFirstName())
            .setLastName(userDto.getLastName())
            .setRoleId(role.getPK())
            .setRole(role.getString())
            .setNumber(userDto.getNumber())
            .setLogin(userDto.getLogin()));
    }

    public User findByLogin(String login) throws DaoException {
        return userDao.findByLogin(login);
    }

    public void save(User user) throws DaoException {
        userDao.store(user);
    }

    public List<UserDtoWithoutPassword> getUsers() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDao.getUsers(user.getPK());
    }

    public void createRequest(long id) {
        userDao.setRequested(id);
    }

    public List<UserDtoWithoutPassword> getRequests() {
        return userDao.getRequests();
    }

    public void decline(long id) {
        userDao.decline(id);
    }

    public void approve(long id) {
        userDao.approve(id);
    }

    public void dismiss(long id) {
        userDao.dismiss(id);
    }

    public List<UserDtoWithoutPassword> getUsersOnEvent(long id) {
        return userDao.getUsersOnEvent(id);
    }

    public List<UserDtoWithoutPassword> getLib() {
        return userDao.getLib();
    }
}
