package ru.pobedonostsev.dbcourse.dao.user;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import ru.pobedonostsev.dbcourse.dao.role.RoleTable;
import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.dto.UserDtoWithoutPassword;
import ru.pobedonostsev.dbcourse.exception.RoleAlreadyExistException;
import ru.pobedonostsev.dbcourse.model.User;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Component
public class UserDao extends BaseDao<Long, User> implements UserTable {
    private static final Log log = LogFactory.getLog(UserDao.class);

    String createSql = "INSERT INTO " + getTableName() + " (" +
        String.join(", ", DATA_FIELDS) + ") VALUES (:" +
        String.join(", :", DATA_FIELDS) + ')';

    private static final String[] DATA_FIELDS = new String[] {
        C_FIRST_NAME,
        C_LAST_NAME,
        C_ROLE_ID,
        C_NUMBER,
        C_LOGIN,
        C_PASSWORD
    };

    @Override
    public void store(User entity) throws DaoException {
        MapSqlParameterSource params = new MapSqlParameterSource(C_FIRST_NAME, entity.getFirstName());
        params.addValue(C_LAST_NAME, entity.getLastName());
        params.addValue(C_LOGIN, entity.getLogin());
        params.addValue(C_PASSWORD, entity.getPassword());
        params.addValue(C_NUMBER, entity.getNumber());
        params.addValue(C_ROLE_ID, entity.getRoleId());
        try {
            client.update(createSql, params);
        } catch (DataIntegrityViolationException e) {
            log.error(e);
            throw new RoleAlreadyExistException("User already exist");
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(User entity) throws DaoException {
        store(entity);
    }

    @Override
    public String getTableName() {
        return "Users";
    }

    @Override
    public String getPKColumn() {
        return C_ID;
    }

    @Override
    public RowMapper<User> getMapper() {
        return (rs, rowNum) -> new User()
            .setId(rs.getLong(C_ID))
            .setFirstName(rs.getString(C_FIRST_NAME))
            .setLastName(rs.getString(C_LAST_NAME))
            .setRoleId(rs.getLong(C_ROLE_ID))
            .setRole(rs.getString(RoleTable.C_NAME))
            .setNumber(rs.getString(C_NUMBER))
            .setLogin(rs.getString(C_LOGIN))
            .setPassword(rs.getString(C_PASSWORD))
            .setRequest(rs.getBoolean("request"));
    }

    public User findByLogin(String login) throws DaoException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(getTableName());
        sb.append(" join role r on r.id = users.role ");
        sb.append(" WHERE ");
        sb.append(C_LOGIN);
        sb.append("=:login");
        String sql = sb.toString();
        try {
            List<User> result = client.query(sql, new MapSqlParameterSource("login", login), getMapper());
            return result.isEmpty() ? null : result.get(0);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User load(Long pk) throws DaoException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT users.id, users.firstname, users.lastname, users.number, users.login, users.password, r.id as role, r.name, users.request FROM ");
        sb.append(getTableName());
        sb.append(" join role r on r.id = users.role ");
        sb.append(" WHERE ");
        sb.append("users.");
        sb.append(getPKColumn());
        sb.append("=:pk");
        String sql = sb.toString();
        try {
            List<User> result = client.query(sql, new MapSqlParameterSource("pk", pk), getMapper());
            return result.isEmpty() ? null : result.get(0);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    private static final String SQL_GET_ALL =
        """
            select * from users
            join role r on r.id = users.role
            where users.id != :id
        """;

    public List<UserDtoWithoutPassword> getUsers(long userId) {
        return client.query(SQL_GET_ALL, new MapSqlParameterSource("id", userId), (rs, rowNum) -> new UserDtoWithoutPassword()
            .setId(rs.getLong(C_ID))
            .setFirstName(rs.getString(C_FIRST_NAME))
            .setLastName(rs.getString(C_LAST_NAME))
            .setRole(rs.getString(RoleTable.C_NAME).replace("ROLE_", ""))
            .setNumber(rs.getString(C_NUMBER))
            .setLogin(rs.getString(C_LOGIN)));
    }

    private static final String SQL_SET_REQUESTED =
        """
            update users set request=true where id=:id;
        """;

    public void setRequested(long id) {
        client.update(SQL_SET_REQUESTED, new MapSqlParameterSource("id", id));
    }

    private static final String SQL_GET_REQUESTED =
        """
            select * from users where request=true;
        """;

    public List<UserDtoWithoutPassword> getRequests() {
        return client.query(SQL_GET_REQUESTED, (rs, rowNum) -> new UserDtoWithoutPassword()
            .setId(rs.getLong(C_ID))
            .setFirstName(rs.getString(C_FIRST_NAME))
            .setLastName(rs.getString(C_LAST_NAME))
            .setNumber(rs.getString(C_NUMBER))
            .setLogin(rs.getString(C_LOGIN)));
    }

    private static final String SQL_SET_REQUESTED_FALSE =
        """
            update users set request=false where id=:id;
        """;

    private static final String SQL_SET_LIBRARIAN =
        """
            update users set role=2 where id=:id;
        """;

    private static final String SQL_SET_CLIENT =
        """
            update users set role=3 where id=:id;
        """;

    public void decline(long id) {
        client.update(SQL_SET_REQUESTED_FALSE, new MapSqlParameterSource("id", id));
    }

    public void approve(long id) {
        client.update(SQL_SET_REQUESTED_FALSE, new MapSqlParameterSource("id", id));
        client.update(SQL_SET_LIBRARIAN, new MapSqlParameterSource("id", id));
    }

    public void dismiss(long id) {
        client.update(SQL_SET_CLIENT, new MapSqlParameterSource("id", id));
    }

    private static final String SQL_GET_EVENT =
        """
            select * from users
            join participants p on users.id = p.user_id
            where p.event_id = :id
        """;

    public List<UserDtoWithoutPassword> getUsersOnEvent(long id) {
        return client.query(SQL_GET_EVENT, new MapSqlParameterSource("id", id), (rs, rowNum) -> new UserDtoWithoutPassword()
            .setId(rs.getLong(C_ID))
            .setFirstName(rs.getString(C_FIRST_NAME))
            .setLastName(rs.getString(C_LAST_NAME))
            .setNumber(rs.getString(C_NUMBER))
            .setLogin(rs.getString(C_LOGIN)));
    }

    private static final String SQL_GET_ALL_LIB =
        """
            select * from users
            join role r on r.id = users.role
            where r.name = 'ROLE_LIBRARIAN';
        """;

    public List<UserDtoWithoutPassword> getLib() {
        return client.query(SQL_GET_ALL_LIB, (rs, rowNum) -> new UserDtoWithoutPassword()
            .setId(rs.getLong(C_ID))
            .setFirstName(rs.getString(C_FIRST_NAME))
            .setLastName(rs.getString(C_LAST_NAME))
            .setNumber(rs.getString(C_NUMBER))
            .setLogin(rs.getString(C_LOGIN)));
    }
}
