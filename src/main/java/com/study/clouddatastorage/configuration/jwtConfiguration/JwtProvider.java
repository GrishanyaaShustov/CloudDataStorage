package com.study.clouddatastorage.configuration.jwtConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtProvider {

    static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JWT_KEY.getBytes());

    // Генерация JWT на основе Authentication
    public static String generateToken(Authentication auth){
        return Jwts.builder()
                .setIssuedAt(new Date()) // время создания
                .setExpiration(new Date(new Date().getTime() + 86400000)) // срок жизни 1 день
                .claim("email", auth.getName()) // сохраняем email
                .signWith(key) // подписываем
                .compact();
    }

    // Получение email из токена
    public static String getEmailFromToken(String jwt){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return String.valueOf(claims.get("email"));
    }
}

