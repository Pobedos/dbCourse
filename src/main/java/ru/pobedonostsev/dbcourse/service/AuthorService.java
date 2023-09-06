package ru.pobedonostsev.dbcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.pobedonostsev.dbcourse.dao.author.AuthorDao;
import ru.pobedonostsev.dbcourse.dao.genre.GenreDao;
import ru.pobedonostsev.dbcourse.model.Author;
import ru.pobedonostsev.dbcourse.model.Genre;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.service.BaseService;

@Service
public class AuthorService extends BaseService<Long, Author> {
    @Autowired
    private AuthorDao authorDao;

    @Override
    protected BaseDao<Long, Author> getDao() {
        return authorDao;
    }
}
