package com.example.shop.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "TestSecretKey";
    private static final long EXPIRATION_TIME = 86400000; // 1일

    // 토큰 생성 시 userId도 포함
    public String generateToken(String username, Long userId, boolean isAdmin) {
        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withClaim("isAdmin", isAdmin)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String extractUsername(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    public Long extractUserId(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userId").asLong();
    }

    public boolean extractIsAdmin(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("isAdmin").asBoolean();
    }
}
