package ru.pobedonostsev.dbcourse.service;

import org.springframework.beans.factory.annotation.Autowired;

import ru.pobedonostsev.dbcourse.dao.genre.GenreDao;
import ru.pobedonostsev.dbcourse.model.Genre;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.service.BaseService;

public class GenreService extends BaseService<Long, Genre> {
    @Autowired
    private GenreDao genreDao;

    @Override
    protected BaseDao<Long, Genre> getDao() {
        return genreDao;
    }
}
