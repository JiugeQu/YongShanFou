package com.mizore.mob.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JWT {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public static final long TTL = 24 * 3600 * 1000;    // 1day
    public static String generateJWT(Map<String, Object> claims) {
        return  Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + TTL * 2))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
