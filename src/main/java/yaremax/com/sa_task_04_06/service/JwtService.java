package yaremax.com.sa_task_04_06.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yaremax.com.sa_task_04_06.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * This class provides service for JWT operations.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.config.security.jwt.secret_key}")
    private String SECRET_KEY;

    /**
     * Extracts subject from JWT token.
     *
     * @param  token          the token to extract subject from
     * @return                the subject from token
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts claim from JWT token.
     *
     * @param   token          the token to extract claim from
     * @param   claimsResolver the function to resolve claim
     * @param   <T>            the type of claim
     * @return                 the resolved claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates JWT token.
     *
     * @param  user  the user to generate token for
     * @return       the generated token
     */
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    /**
     * Generates JWT token with extra claims.
     *
     * @param   extraClaims  the extra claims to add to token
     * @param   user         the user to generate token for
     * @return               the generated token
     */
    public String generateToken(Map<String, Object> extraClaims,
                                User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if JWT token is valid for specified user.
     *
     * @param  token  the token to check
     * @param  user   the user to check token for
     * @return        true if token is valid, false otherwise
     */
    public boolean isTokenValid(String token, User user) {
        final String username = extractSubject(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if JWT token is expired.
     *
     * @param  token  the token to check
     * @return        true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts expiration date from JWT token.
     *
     * @param  token  the token to extract expiration date from
     * @return        the expiration date from token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from JWT token.
     *
     * @param   token  the token to extract claims from
     * @return         the extracted claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generates signing key.
     *
     * @return the signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
