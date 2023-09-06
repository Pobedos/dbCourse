package ru.pobedonostsev.dbcourse.dao.role;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import ru.pobedonostsev.dbcourse.model.Role;
import ru.pobedonostsev.framework.dao.StringDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Component
public class RoleDao extends StringDao<Long, Role> implements RoleTable {

    @Override
    public String getStringColumn() {
        return C_NAME;
    }

    @Override
    public void update(Role entity) throws DaoException {
        store(entity);
    }

    @Override
    public String getTableName() {
        return "Role";
    }

    @Override
    public String getPKColumn() {
        return C_ID;
    }

    @Override
    public RowMapper<Role> getMapper() {
        return (rs, rowNum) -> new Role(rs.getLong(C_ID), rs.getString(C_NAME));
    }

    private static final String GET_BY_NAME =
        """
            SELECT id, name FROM role
            WHERE name=:name
        """;

    public Role getByName(String role) {
        List<Role> result = client.query(GET_BY_NAME, new MapSqlParameterSource(C_NAME, role), getMapper());
        return result.isEmpty() ? null : result.get(0);
    }
}
