package com.smartjob.userreg.infrastructure.security;


import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JWTProvider {

    @Value("${jwt.secret}") private String secret;

    public static final String TOKEN_PARAM = "TOKEN_SJ";

    /**
     * Verifies if the provided token is a valid JWT token. .
     * @param token jwt token
     * @return Returns true if it is a valid token, otherwise false
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Generates a JWT token
     * @return JWT token generated
     */
    public String generateToken() {
         JwtBuilder jwtBuilder = Jwts
                .builder()
                .issuedAt(new Date())
                .signWith(getSecretKey());
         return jwtBuilder.compact();
    }

    /**
     * Generates a SecretKey instance based on the specified secret key
     * @return SecretKey
     */
    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
