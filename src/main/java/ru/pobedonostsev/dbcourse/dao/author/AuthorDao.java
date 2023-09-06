package ru.pobedonostsev.dbcourse.dao.author;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.pobedonostsev.dbcourse.model.Author;
import ru.pobedonostsev.dbcourse.model.Genre;
import ru.pobedonostsev.framework.dao.StringDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Repository
public class AuthorDao extends StringDao<Long, Author> implements AuthorTable {
    @Override
    public void update(Author entity) throws DaoException {
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
    public RowMapper<Author> getMapper() {
        return (rs, rowNum) -> new Author().setId(rs.getLong(C_ID)).setName(rs.getString(C_NAME));
    }

    @Override
    public String getStringColumn() {
        return C_NAME;
    }
}
