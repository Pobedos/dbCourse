package ru.pobedonostsev.dbcourse.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKey";
    private long accessTtl = 24 * 60 * 60 * 1000;
    private long refreshTtl = 3 * 24 * 60 * 60 * 1000;

    public String getSecretKey() {
        return secretKey;
    }

    public JwtProperties setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public long getAccessTtl() {
        return accessTtl;
    }

    public JwtProperties setAccessTtl(long accessTtl) {
        this.accessTtl = accessTtl;
        return this;
    }

    public long getRefreshTtl() {
        return refreshTtl;
    }

    public JwtProperties setRefreshTtl(long refreshTtl) {
        this.refreshTtl = refreshTtl;
        return this;
    }
}
