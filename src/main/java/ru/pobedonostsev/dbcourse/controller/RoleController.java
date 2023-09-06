package ru.pobedonostsev.dbcourse.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.pobedonostsev.dbcourse.model.Role;
import ru.pobedonostsev.dbcourse.service.RoleService;
import ru.pobedonostsev.framework.exception.DaoException;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private static final Log log = LogFactory.getLog(RoleController.class);

    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    public Role getRole(@PathVariable Long id) throws DaoException {
        return roleService.get(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteRole(@RequestParam Long id) throws DaoException {
        roleService.delete(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createRole(@RequestBody String roleName) throws DaoException {
        roleService.create(roleName);
    }
}
