package ru.pobedonostsev.dbcourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.pobedonostsev.dbcourse.model.Author;
import ru.pobedonostsev.dbcourse.service.AuthorService;
import ru.pobedonostsev.framework.exception.DaoException;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping("/{id}")
    public Author getAuthor(@PathVariable long id) throws DaoException {
        return authorService.get(id);
    }
}
