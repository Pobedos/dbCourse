package ru.pobedonostsev.dbcourse.dao.bookState;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.pobedonostsev.dbcourse.model.BookState;
import ru.pobedonostsev.framework.dao.StringDao;
import ru.pobedonostsev.framework.exception.DaoException;

@Repository
public class BookStateDao extends StringDao<Long, BookState> implements BookStateTable {
    @Override
    public void update(BookState entity) throws DaoException {
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
    public RowMapper<BookState> getMapper() {
        return (rs, rowNum) -> new BookState().setId(rs.getLong(C_ID)).setName(rs.getString(C_STATE));
    }

    @Override
    public String getStringColumn() {
        return C_STATE;
    }
}
