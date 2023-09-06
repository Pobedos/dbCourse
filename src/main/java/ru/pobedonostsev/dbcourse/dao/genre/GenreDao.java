package ru.pobedonostsev.dbcourse.dao.genre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.pobedonostsev.dbcourse.model.Genre;
import ru.pobedonostsev.framework.dao.StringDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Repository
public class GenreDao extends StringDao<Long, Genre> implements GenreTable {
    @Override
    public void update(Genre entity) throws DaoException {
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
    public RowMapper<Genre> getMapper() {
        return (rs, rowNum) -> new Genre().setId(rs.getLong(C_ID)).setName(rs.getString(C_NAME));
    }

    @Override
    public String getStringColumn() {
        return C_NAME;
    }
}
