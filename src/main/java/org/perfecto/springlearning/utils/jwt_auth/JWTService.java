package org.perfecto.springlearning.utils.jwt_auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.perfecto.springlearning.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    public String generateToken(Student student, Map<String, Object> extraClaims) {
        // here we will generate the token using the student information and the secret key and the expiration time and return the token to the client
        return Jwts.builder().claims(extraClaims) // here we are adding the extra claims to the token which can be any additional information that we want to include in the token apart from the standard claims like subject, issuedAt, expiration etc.
                .subject(student.getId().toString()) // here we are setting the subject of the token to be the student's id which is a unique identifier for each student and it is a good practice to use the unique identifier of the user as the subject of the token because it can be used to identify the user in the future when we want to validate the token and we can also use other information like email or username as the subject but using the unique identifier is more reliable and secure because it cannot be easily guessed or forged by an attacker
                .issuedAt(new java.util.Date(System.currentTimeMillis())) // here we are setting the issuedAt claim of the token to be the current time which is the time when the token is generated and it is a good practice to set the issuedAt claim because it can be used to determine the age of the token and it can also be used to invalidate the token if it is too old or if it has been compromised
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // here we are setting the expiration claim of the token to be the current time plus the expiration time which is the time when the token will expire and it is a good practice to set the expiration claim because it can be used to invalidate the token after a certain period of time and it can also be used to prevent the token from being used indefinitely if it has been compromised
                .signWith(getSigningKey()) // here we are signing the token with the secret key using the HMAC SHA algorithm which is a symmetric algorithm that uses the same key for signing and verifying the token and it is a good practice to use a strong secret key that is at least 256 bits long and it should be kept secret and not shared with anyone else because if an attacker gets access to the secret key then they can easily forge the token and gain unauthorized access to the system
                .compact();
    }

    // this is an overloaded method for generating token without extra claims this
    public String generateToken(Student student) {
        return generateToken(student, Map.of());
    }

    // here we will validate the token by parsing it and verifying the signature and the expiration time and return true if the token is valid and false if the token is invalid
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().
                verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // here the extractClaim method is a generic method that takes the token and a function that takes the claims and returns the desired claim and it uses the getClaimsFromToken method to get the claims from the token and then applies the function to the claims to extract the desired claim and return it to the caller
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // here we will validate the token by checking if the token is expired or not and if the token is valid then we will return true and if the token is invalid then we will return false
    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;

        }
            catch (SignatureException e) {
                logger.error("Invalid JWT signature: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                logger.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims string is empty: {}", e.getMessage());
            }

            return false;

    }

    public String getStudentIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }
}
