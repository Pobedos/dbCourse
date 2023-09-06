package ru.pobedonostsev.dbcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.pobedonostsev.dbcourse.dao.bookState.BookStateDao;
import ru.pobedonostsev.dbcourse.model.BookState;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.service.BaseService;

@Service
public class BookStateService extends BaseService<Long, BookState> {
    @Autowired
    private BookStateDao bookStateDao;

    @Override
    protected BaseDao<Long, BookState> getDao() {
        return bookStateDao;
    }
}
