package ru.pobedonostsev.dbcourse.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.stream.MalformedJsonException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ru.pobedonostsev.dbcourse.config.security.JwtProperties;
import ru.pobedonostsev.dbcourse.dao.token.TokenDao;
import ru.pobedonostsev.dbcourse.dto.UserDto;
import ru.pobedonostsev.dbcourse.exception.UnauthorizedException;
import ru.pobedonostsev.dbcourse.model.Token;
import ru.pobedonostsev.dbcourse.model.User;
import ru.pobedonostsev.framework.dao.BaseDao;
import ru.pobedonostsev.framework.exception.DaoException;
import ru.pobedonostsev.framework.service.BaseService;

@Component
public class TokenService extends BaseService<Long, Token> {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private TokenDao tokenDao;

    private Key secretKey;

    @PostConstruct
    private void init() {
        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private String generateToken(User user, long ttl) {
        Claims claims = Jwts.claims()
            .setSubject(user.getLogin());
        claims.put("role", user.getRole());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ttl);

        return Jwts.builder()
            .addClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(expirationDate)
            .signWith(secretKey)
            .compact();
    }

    public String generateAccess(User user) {
        return generateToken(user, jwtProperties.getAccessTtl());
    }

    public String generateRefresh(User user) {
        return generateToken(user, jwtProperties.getRefreshTtl());
    }

    public void removeToken(String refreshToken) {
        tokenDao.removeByRefreshToken(refreshToken);
    }

    public UserDto validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            if (body.getExpiration().before(new Date())) {
                throw new UnauthorizedException();
            }
            return new UserDto().setLogin(body.getSubject()).setRole((String) body.get("role"));
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    public Token findToken(String refreshToken) {
        return tokenDao.getByRefresh(refreshToken);
    }

    @Override
    protected BaseDao<Long, Token> getDao() {
        return tokenDao;
    }

    public void save(Long userId, String refreshToken) throws DaoException {
        tokenDao.store(new Token().setUserId(userId).setRefreshToken(refreshToken));
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
