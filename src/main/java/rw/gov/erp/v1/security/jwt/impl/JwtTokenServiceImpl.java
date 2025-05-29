package rw.gov.erp.v1.security.jwt.impl;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import rw.gov.erp.v1.security.jwt.JwtTokenService;
import rw.gov.erp.v1.security.user.UserPrincipal;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

  @Value("${application.security.jwt.secret-key}")
  private String JWT_SECRET_KEY;

  @Value("${application.security.jwt.expiration}")
  private Long JWT_EXPIRATION;

  private Key key;

  @PostConstruct
  public void init() {
    byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  @Override
  public String generateToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .claim("id", userPrincipal.getId())
        .claim("roles", userPrincipal.getAuthorities())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractEmailFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  @Override
  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  @Override
  public String extractEmailFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
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

}
