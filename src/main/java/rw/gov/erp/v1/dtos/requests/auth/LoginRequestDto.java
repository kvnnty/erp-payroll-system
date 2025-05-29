package rw.gov.erp.v1.dtos.requests.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

  @NotBlank(message = "Please provide your email address")
  @Email(message = "Please enter a valid email address")
  private String email;

  @NotBlank(message = "Please provide your password")
  private String password;
}
