package com.project.usermicroservice.authConfig;

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

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${api.secretkey}")
    private String secretKey;
    @Value("${expire.jw-token}")
    private int jwtTokenExpire;

    /*Generate an access token*/
    public String generateToken(UserDetails userDetails, Long userId) {
        Map<String, String> extraClaims = new HashMap<>();
        String authString = userDetails.getAuthorities().toString();
        extraClaims.put("Roles", authString.substring(1, authString.length() - 1));
        extraClaims.put("UserId", userId.toString());
        return generateToken(extraClaims, userDetails, jwtTokenExpire);
    }

    /*Method to generate the token*/
    public String generateToken(Map<String, String> extraClaims, UserDetails userDetails, int expireDuration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireDuration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /*Decode the secret key and get the signIn key*/
    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
