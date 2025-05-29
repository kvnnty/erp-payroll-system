package rw.gov.erp.v1.dtos.requests.employee;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.erp.v1.validation.annotations.ValidPassword;
import rw.gov.erp.v1.validation.annotations.ValidPhoneNumber;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDto {
  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "Please enter a valid email address")
  private String email;

  @NotBlank(message = "Password is required")
  @ValidPassword
  private String password;

  @NotBlank(message = "Phone number is required")
  @ValidPhoneNumber
  private String phoneNumber;

  @Past(message = "Please enter a valid date of birth, date of birth must be some time in the past")
  @NotNull(message = "Date of birth is required")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;

}
