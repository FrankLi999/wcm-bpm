package com.bpwizard.myresources.service.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.bpwizard.myresources.resource.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class DefaultJwtService implements JwtService {
    private String secret;
    private int sessionTime;

    @Autowired
    public DefaultJwtService(@Value("${jwt.secret}") String secret,
                             @Value("${jwt.sessionTime}") int sessionTime) {
        this.secret = secret;
        this.sessionTime = sessionTime;
    }

    @Override
    public String toToken(User user) {
        return Jwts.builder()
            .setSubject(user.getId().toString())
            .setExpiration(expireTimeFromNow())
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    @Override
    public Optional<Long> getSubFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Long userId = claimsJws.getBody().getSubject() == null ? null : Long.getLong(claimsJws.getBody().getSubject());
            return Optional.ofNullable(userId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Date expireTimeFromNow() {
        return new Date(System.currentTimeMillis() + sessionTime * 1000);
    }
}
