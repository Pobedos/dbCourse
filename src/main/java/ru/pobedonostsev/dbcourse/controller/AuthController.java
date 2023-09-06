package ru.pobedonostsev.dbcourse.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.pobedonostsev.dbcourse.config.security.JwtProperties;
import ru.pobedonostsev.dbcourse.dto.AuthDto;
import ru.pobedonostsev.dbcourse.dto.AuthResponse;
import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.service.AuthService;
import ru.pobedonostsev.framework.exception.DaoException;

@RestController
@RequestMapping("/api")
public class AuthController {
    public static final String REFRESH_TOKEN = "refreshToken";
    private static final Log log = LogFactory.getLog(AuthController.class);
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody UserDto userDto, HttpServletResponse response) throws DaoException {
        AuthResponse result = authService.register(userDto);
        HttpCookie cookie = ResponseCookie.from(REFRESH_TOKEN, result.getRefreshToken())
            .maxAge(jwtProperties.getRefreshTtl())
            .httpOnly(true)
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return result;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthDto authDto, HttpServletResponse response) throws DaoException {
        AuthResponse tokens = authService.login(authDto);
        HttpCookie cookie = ResponseCookie.from(REFRESH_TOKEN, tokens.getRefreshToken())
            .maxAge(jwtProperties.getRefreshTtl())
            .httpOnly(true)
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return tokens;
    }

    @PostMapping("/logout")
    public void logout(@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken, HttpServletResponse response) {
        authService.logout(refreshToken);
        HttpCookie cookie = ResponseCookie.from(REFRESH_TOKEN, "")
            .maxAge(0)
            .httpOnly(true)
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @GetMapping("/refresh")
    public AuthResponse refresh(@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken, HttpServletResponse response) throws DaoException {
        AuthResponse tokens = authService.refresh(refreshToken);
        HttpCookie cookie = ResponseCookie.from(REFRESH_TOKEN, tokens.getRefreshToken())
            .maxAge(jwtProperties.getRefreshTtl())
            .httpOnly(true)
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return tokens;
    }
}
