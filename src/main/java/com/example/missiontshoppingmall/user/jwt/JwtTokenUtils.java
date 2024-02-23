package com.example.missiontshoppingmall.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey;
    private final JwtParser jwtParser;

    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
    }

    // UserDetails를 받아서 JWT로 변환
    // header - payload : claim, sub, iat, exp - signature
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now(); //시간
        Claims jwtClaims = Jwts
                .claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(36000L)));

        // jwt 발급
        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.signingKey)
                .compact(); //압축

    }

    // 정상적인 jwt인지 판단
    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("invalid jwt");
        }
        return false;
    }

    // payload를 반환
    public Claims parseClaims(String token) {
        log.info("parseClaims:::" + jwtParser.parseClaimsJws(token).getBody());
        return jwtParser.parseClaimsJws(token).getBody();
    }

}
