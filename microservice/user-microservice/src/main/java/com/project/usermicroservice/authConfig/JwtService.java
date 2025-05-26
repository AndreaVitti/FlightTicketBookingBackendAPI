package com.project.usermicroservice.authConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${api.secretkey}")
    private String secretKey;
    @Value("${expire.access-token}")
    private int accessTokenExpire;
    @Value("${expire.refresh-token}")
    private int refreshTokenExpire;

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(UserDetails userDetails, Long userId) {
        Map<String, String> extraClaims = new HashMap<>();
        String authString = userDetails.getAuthorities().toString();
        extraClaims.put("Roles", authString.substring(1, authString.length() - 1));
        extraClaims.put("UserId", userId.toString());
        return generateAccessToken(extraClaims, userDetails, accessTokenExpire);
    }

    public String generateRefreshToken(Long userId) {
        return generateRefreshToken(new HashMap<>(), userId, refreshTokenExpire);
    }

    public String generateAccessToken(Map<String, String> extraClaims, UserDetails userDetails, int expireDuration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireDuration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, String> extraClaims, Long userId, int expireDuration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireDuration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
