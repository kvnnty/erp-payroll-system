package rw.gov.erp.v1.payload;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
  private boolean success;
  private String message;
  private HttpStatus status;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T payload;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ApiError error;

  // --- SUCCESS RESPONSE FACTORIES ---

  public static <T> ResponseEntity<ApiResponse<T>> success(String message, HttpStatus status, T payload) {
    return ResponseEntity.status(status).body(ApiResponse.<T>builder()
        .success(true)
        .message(message)
        .status(status)
        .payload(payload)
        .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> success(String message, T payload) {
    return success(message, HttpStatus.OK, payload);
  }

  public static <T> ResponseEntity<ApiResponse<T>> success(T payload) {
    return success("Request successful", HttpStatus.OK, payload);
  }

  // --- FAIL RESPONSE FACTORIES ---

  public static <T> ResponseEntity<ApiResponse<T>> fail(String message, HttpStatus status, ApiError error) {
    return ResponseEntity.status(status).body(ApiResponse.<T>builder()
        .success(false)
        .message(message)
        .status(status)
        .error(error)
        .build());
  }

  public static <T> ResponseEntity<ApiResponse<T>> fail(String message, ApiError error) {
    return fail(message, HttpStatus.BAD_REQUEST, error);
  }

  public static ResponseEntity<ApiResponse<ApiError>> fail(ApiError error) {
    return fail("Request failed", HttpStatus.BAD_REQUEST, error);
  }
}
