package rw.gov.erp.v1.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import rw.gov.erp.v1.exceptions.BadRequestException;
import rw.gov.erp.v1.exceptions.DuplicateResourceException;
import rw.gov.erp.v1.exceptions.FileException;
import rw.gov.erp.v1.exceptions.ResourceNotFoundException;
import rw.gov.erp.v1.payload.ApiError;
import rw.gov.erp.v1.payload.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<ApiResponse<ApiError>> buildApiError(HttpStatus status, String message,
      ApiError errorDetails) {
    return ApiResponse.fail(message, status, errorDetails);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleValidationExceptions(MethodArgumentNotValidException e) {
    FieldError firstError = (FieldError) e.getBindingResult().getAllErrors().get(0);
    String errorMessage = firstError.getDefaultMessage();
    log.debug("USER_INPUT_VALIDATION_ERROR {}", e.getMessage());
    return buildApiError(HttpStatus.BAD_REQUEST, errorMessage,
        ApiError.builder().code("VALIDATION_ERROR").details(null).build());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleUserNotFoundException(UsernameNotFoundException e) {
    log.debug("USER_NOT_FOUND {}", e.getMessage());
    return buildApiError(HttpStatus.NOT_FOUND, e.getMessage(),
        ApiError.builder().code("USER_NOT_FOUND").details(null).build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleBadRequest(BadRequestException e) {
    log.debug("BAD_REQUEST_EXCEPTION {}", e.getMessage());
    return buildApiError(HttpStatus.BAD_REQUEST, e.getMessage(),
        ApiError.builder().code("BAD_REQUEST").details(null).build());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleResourceNotFoundException(ResourceNotFoundException e) {
    log.debug("RESOURCE_NOT_FOUND {}", e.getMessage());
    return buildApiError(HttpStatus.NOT_FOUND, e.getMessage(),
        ApiError.builder().code("RESOURCE_NOT_FOUND").details(null).build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleInvalidJson(HttpMessageNotReadableException e) {
    log.debug("MALFORMED_JSON_REQUEST {}", e.getMessage());
    return buildApiError(HttpStatus.BAD_REQUEST, "Malformed JSON request",
        ApiError.builder().code("MALFORMED_JSON_REQUEST").details(null).build());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleMethodNotAllowedException(
      HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
    log.debug("METHOD_NOT_ALLOWED {}", e.getMessage());
    return buildApiError(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage(),
        ApiError.builder().code("METHOD_NOT_ALLOWED").details(null).build());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleNotFound(NoResourceFoundException e, HttpServletRequest request) {
    log.error("HANDLER_NOT_FOUND {}", e.getMessage());
    return buildApiError(HttpStatus.NOT_FOUND, "API endpoint not found",
        ApiError.builder().code("ENDPOINT_NOT_FOUND").details(null).build());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleAccessDeniedException(AccessDeniedException e) {
    log.error("UNAUTHORIZED_REQUEST {}", e.getMessage());
    return buildApiError(HttpStatus.FORBIDDEN, "You are not authorized to access this resource",
        ApiError.builder().code("ACCESS_DENIED").details(null).build());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleBadCredentialsException(BadCredentialsException e,
      HttpServletRequest request) {
    log.debug("INVALID_CREDENTIALS {}", e.getMessage());
    return buildApiError(HttpStatus.BAD_REQUEST, e.getMessage(),
        ApiError.builder().code("INVALID_CREDENTIALS").details(null).build());
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleConflictingResourceException(DuplicateResourceException e,
      HttpServletRequest request) {
    log.debug("DUPLICATE_RESOURCE_EXCEPTION {}", e.getMessage());
    return buildApiError(HttpStatus.CONFLICT, e.getMessage(),
        ApiError.builder().code("DUPLICATE_RESOURCE").details(null).build());
  }

  @ExceptionHandler(FileException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleFileException(FileException e, HttpServletRequest request) {
    log.debug("FILE_EXCEPTION {}", e.getMessage());
    return buildApiError(HttpStatus.BAD_REQUEST, e.getMessage(),
        ApiError.builder().code("FILE_ERROR").details(null).build());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<ApiError>> handleIllegalArgumentException(IllegalArgumentException e,
      HttpServletRequest request) {
    log.debug("ILLEGAL_ARGUMENT {}", e.getMessage());
    return buildApiError(HttpStatus.BAD_REQUEST, e.getMessage(),
        ApiError.builder().code("ILLEGAL_ARGUMENT").details(null).build());
  }

  @ExceptionHandler({ RuntimeException.class, Exception.class })
  public ResponseEntity<ApiResponse<ApiError>> handleRuntimeException(Exception e, HttpServletRequest request) {
    log.error("UNHANDLED_RUNTIME_EXCEPTION at {}: {}", request.getRequestURI(), e.getMessage(), e);
    return buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong",
        ApiError.builder().code("SERVER_ERROR").details(null).build());
  }

}
