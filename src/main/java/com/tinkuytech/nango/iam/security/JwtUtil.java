package com.tinkuytech.nango.iam.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import com.tinkuytech.nango.iam.entity.Role;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor("01234567890123456789012345678901".getBytes());

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            return (Long) userId;
        } else {
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username, Set<Role> roles) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        claims.put("roles", roleNames);
        return createToken(claims, username);
    }

    public String generateToken(Long userId, String username, Set<Role> roles) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        claims.put("roles", roleNames);
        claims.put("userId", userId);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
