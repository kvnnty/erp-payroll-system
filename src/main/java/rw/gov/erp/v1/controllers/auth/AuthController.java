package rw.gov.erp.v1.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.auth.LoginRequestDto;
import rw.gov.erp.v1.dtos.responses.auth.AuthResponseDTO;
import rw.gov.erp.v1.payload.ApiResponse;
import rw.gov.erp.v1.services.auth.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authentication Resource", description = "APIs for user authentication operations")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDto requestDTO) {
    AuthResponseDTO authResponse = authService.login(requestDTO);
    return ApiResponse.success("Logged in successfully", HttpStatus.OK, authResponse);
  }
}
