package rw.gov.erp.v1.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
  String generateToken(Authentication authentication);

  boolean isTokenValid(String token, UserDetails userDetails);

  boolean isTokenExpired(String token);

  String extractEmailFromToken(String token);
}
