package ru.pobedonostsev.dbcourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ru.pobedonostsev.dbcourse.config.security.JwtProperties;
import ru.pobedonostsev.dbcourse.dto.AuthDto;
import ru.pobedonostsev.dbcourse.dto.AuthResponse;
import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.exception.BadRequestException;
import ru.pobedonostsev.dbcourse.exception.LoginAlreadyExistException;
import ru.pobedonostsev.dbcourse.exception.UnauthorizedException;
import ru.pobedonostsev.dbcourse.exception.UserDoesNotExistException;
import ru.pobedonostsev.dbcourse.model.Role;
import ru.pobedonostsev.dbcourse.model.Token;
import ru.pobedonostsev.dbcourse.model.User;
import ru.pobedonostsev.framework.exception.DaoException;
import ru.pobedonostsev.framework.utils.StringUtils;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RoleService roleService;

    public AuthResponse register(UserDto userDto) throws DaoException {
        User candidate = userService.findByLogin(userDto.getLogin());
        if (candidate != null) {
            throw new LoginAlreadyExistException("Логин уже занят!");
        }

        Role role = roleService.getByName(userDto.getRole());

        User user = new User()
            .setLogin(userDto.getLogin())
            .setPassword(passwordEncoder.encode(userDto.getPassword()))
            .setNumber(userDto.getNumber())
            .setFirstName(userDto.getFirstName())
            .setLastName(userDto.getLastName())
            .setRoleId(role.getPK())
            .setRole(userDto.getRole());
        userService.save(user);
        String accessToken = tokenService.generateAccess(user);
        String refreshToken = tokenService.generateRefresh(user);
        return new AuthResponse().setAccessToken(accessToken).setRefreshToken(refreshToken);
    }

    public AuthResponse login(AuthDto authDto) throws DaoException {
        User candidate = userService.findByLogin(authDto.getLogin());
        if (candidate == null) {
            throw new BadRequestException("Нет такого пользователя");
        }
        boolean equalPasswords = passwordEncoder.matches(authDto.getPassword(), candidate.getPassword());
        if (!equalPasswords) {
            throw new BadRequestException("Неверный пароль");
        }
        String access = tokenService.generateAccess(candidate);
        String refresh = tokenService.generateRefresh(candidate);
        tokenService.save(candidate.getPK(), refresh);
        return new AuthResponse().setAccessToken(access).setRefreshToken(refresh).setUser(candidate);
    }

    public void logout(String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new UnauthorizedException();
        }
        tokenService.removeToken(refreshToken);
    }

    public AuthResponse refresh(String refreshToken) throws DaoException {
        if (StringUtils.isEmpty(refreshToken)) {
            throw new UnauthorizedException();
        }
        UserDto userDto = tokenService.validateToken(refreshToken);
        Token token = tokenService.findToken(refreshToken);
        if (token == null) {
            throw new UnauthorizedException();
        }
        User user = userService.findByLogin(userDto.getLogin());
        String access = tokenService.generateAccess(user);
        String refresh = tokenService.generateRefresh(user);

        tokenService.save(user.getPK(), refresh);
        return new AuthResponse().setAccessToken(access).setRefreshToken(refresh).setUser(user);
    }
}
