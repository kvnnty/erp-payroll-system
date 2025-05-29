package rw.gov.erp.v1.dtos.requests.employee;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.erp.v1.validation.annotations.ValidPhoneNumber;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDto {

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Please enter a valid email address")
  private String email;

  @NotBlank(message = "Phone number cannot be blank")
  @ValidPhoneNumber
  private String phoneNumber;

  private LocalDate dateOfBirth;

}
