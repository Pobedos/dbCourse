package ru.pobedonostsev.dbcourse.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.exception.UnauthorizedException;
import ru.pobedonostsev.dbcourse.model.User;
import ru.pobedonostsev.dbcourse.service.TokenService;
import ru.pobedonostsev.dbcourse.service.UserService;
import ru.pobedonostsev.framework.exception.DaoException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = tokenService.resolveToken(request);
            if (accessToken == null) {
                throw new UnauthorizedException();
            }
            UserDto userDto = tokenService.validateToken(accessToken);
            User user = userService.findByLogin(userDto.getLogin());
            if (user != null) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
            }
        } catch (DaoException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (UnauthorizedException e) {
            // Nothing
        }
        filterChain.doFilter(request, response);
    }
}
