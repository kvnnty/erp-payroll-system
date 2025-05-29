package rw.gov.erp.v1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtVerificationException extends RuntimeException {
  public JwtVerificationException(String message) {
    super(message);
  }
}
