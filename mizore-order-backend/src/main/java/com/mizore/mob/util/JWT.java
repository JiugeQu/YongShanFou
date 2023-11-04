package com.mizore.mob.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JWT {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public static final long TTL = 24 * 3600 * 1000;    // 1day  单位ms
    public static String generateJWT(Map<String, Object> claims) {
        return  Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + TTL))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 使用本机密钥解析
     * @param jwt
     * @return
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    /**
     * 使用传来的密钥解析
     * @param jwt
     * @param secretKey
     * @return
     */
    public static Claims parseJWT(String jwt, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    /**
     * 将本机的SECRET_KEY转字符串
     * @return
     */
    public static String getSecretKeyStr() {
        return Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded());
    }

    /**
     * 接收字符串还原成SecretKey
     */
    public static SecretKey getSecretKey(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }
}
