package ru.pobedonostsev.dbcourse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.pobedonostsev.dbcourse.dto.IdDto;
import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.dto.UserDtoWithoutPassword;
import ru.pobedonostsev.dbcourse.model.User;
import ru.pobedonostsev.dbcourse.service.UserService;
import ru.pobedonostsev.framework.exception.DaoException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws DaoException {
        return userService.get(id);
    }

    @GetMapping
    public List<UserDtoWithoutPassword> getUsers() throws DaoException {
        return userService.getUsers();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@RequestParam Long id) throws DaoException {
        userService.delete(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRole(@RequestBody UserDto userDto) throws DaoException {
        userService.create(userDto);
    }

    @PostMapping("/work")
    public void createRole(@RequestBody IdDto idDto) throws DaoException {
        userService.createRequest(idDto.getId());
    }

    @GetMapping("/requests")
    public List<UserDtoWithoutPassword> createRole() throws DaoException {
        return userService.getRequests();
    }

    @GetMapping("/event/{id}")
    public List<UserDtoWithoutPassword> getUsersOnEvent(@PathVariable long id) throws DaoException {
        return userService.getUsersOnEvent(id);
    }

    @GetMapping("/lib")
    public List<UserDtoWithoutPassword> getUsersOnEvent() throws DaoException {
        return userService.getLib();
    }

    @PostMapping("/decline")
    public void decline(@RequestBody IdDto idDto) throws DaoException {
        userService.decline(idDto.getId());
    }

    @PostMapping("/dismiss")
    public void dismiss(@RequestBody IdDto idDto) throws DaoException {
        userService.dismiss(idDto.getId());
    }

    @PostMapping("/approve")
    public void approve(@RequestBody IdDto idDto) throws DaoException {
        userService.approve(idDto.getId());
    }
}
