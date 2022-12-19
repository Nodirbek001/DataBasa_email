package com.example.databasa_email.security;

import com.example.databasa_email.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private static final long expireTime = 1000 * 60 * 60;
    private static final String secrekeye = "hello";

    public static String GenerateToken(String username, Set<Role> roles) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secrekeye)
                .compact();

    }


    public String GmailFromToken(String token) {
        try {
            String subject = Jwts
                    .parser()
                    .setSigningKey(secrekeye)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return subject;
        } catch (Exception e) {
            return null;
        }
    }
}
