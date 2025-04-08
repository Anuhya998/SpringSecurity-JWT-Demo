package com.anu.securitydemo.service;

import com.anu.securitydemo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Slf4j
@Service
public class JwtService {

    private String secretKey = null;
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getUserName())
                .issuer("ANU!")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60*10*1000))
                .and()
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decode = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }

    public String getSecretKey() {
        return secretKey = "7b333bf8f84f40c389bce76d577835d978c5be31383d50198d9819e3e7591604a80b2c8459c7cb2c7e6ffcd0d9519750a7814513d67aa466b1d0e3b8b780fd44";
    }

    public String extractUserName(String jwtToken) {
        log.info("inside extract usernmae method of jwt service");
        return extractClaims(jwtToken, Claims::getSubject);
    }

    private <T>T extractClaims(String jwtToken, Function<Claims,T> claimResolver) {
        log.info("inside extract claims");
        Claims claims = extractClaims(jwtToken);
        return claimResolver.apply(claims);

    }

    private Claims extractClaims(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        log.info("checking if token is valid");
        final String userName = extractUserName(jwtToken);
        log.info("extracted user name from token: " + userName);
        log.info("extracted user name from db: " + userDetails.getUsername());
        log.info("is token expired?", isJwtTokenExpired(jwtToken));
        return userName.equals(userDetails.getUsername()) && !isJwtTokenExpired(jwtToken);
    }

    private boolean isJwtTokenExpired(String jwtToken) {
        log.info("expiration:", extractExpiration(jwtToken));
        return extractExpiration(jwtToken).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken, Claims::getExpiration);
    }
}
