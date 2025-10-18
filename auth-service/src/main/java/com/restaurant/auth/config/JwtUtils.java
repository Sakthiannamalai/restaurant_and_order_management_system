package com.restaurant.auth.config;

import io.jsonwebtoken.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final long jwtExpiration = 3600000L; // 1 hour

    public JwtUtils() throws Exception {
        this.privateKey = loadPrivateKey("keys/private.pem");
        this.publicKey = loadPublicKey("keys/public.pem");
    }

    private PrivateKey loadPrivateKey(String resourcePath) throws Exception {
        InputStream inputStream = new ClassPathResource(resourcePath).getInputStream();
        String key = new String(inputStream.readAllBytes())
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey(String resourcePath) throws Exception {
        InputStream inputStream = new ClassPathResource(resourcePath).getInputStream();
        String key = new String(inputStream.readAllBytes())
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    public String generateJwtToken(com.restaurant.auth.entity.User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("tenantId", user.getTenantId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
