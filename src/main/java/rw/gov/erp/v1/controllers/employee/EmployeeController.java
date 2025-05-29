package rw.gov.erp.v1.controllers.employee;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.employee.ChangePasswordDto;
import rw.gov.erp.v1.dtos.requests.employee.CreateEmployeeDto;
import rw.gov.erp.v1.dtos.requests.employee.UpdateEmployeeDto;
import rw.gov.erp.v1.dtos.responses.user.UserResponseDTO;
import rw.gov.erp.v1.payload.ApiResponse;
import rw.gov.erp.v1.services.employee.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Resource", description = "APIs for managing Employee account related operations")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeService userService;

  @PostMapping("/register")
  @PreAuthorize("hasRole('MANAGER')")
  @Operation(summary = "Register new employee", description = "Create a new employee account (accessible to MANAGER only)")
  public ResponseEntity<ApiResponse<Object>> registerCustomer(@Valid @RequestBody CreateEmployeeDto requestDTO) {
    userService.registerUser(requestDTO);
    return ApiResponse.success("Employee account has been created successfully", HttpStatus.CREATED, null);
  }

  @GetMapping("/me")
  @Operation(summary = "Get my profile", description = "Retrieve the profile details of the currently authenticated user")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getMyProfile() {
    return ApiResponse.success("User profile retrieved", HttpStatus.OK, userService.getMyprofile());
  }

  @PutMapping("/update-profile")
  @Operation(summary = "Update profile", description = "Update the profile details of the currently authenticated user")
  public ResponseEntity<ApiResponse<Object>> updateProfile(@Valid @RequestBody UpdateEmployeeDto updateDTO) {
    userService.updateEmployeeProfile(updateDTO);
    return ApiResponse.success("Profile updated successfully", HttpStatus.OK, null);
  }

  @PostMapping("/change-password")
  @Operation(summary = "Change password", description = "Change the password of the currently authenticated user")
  public ResponseEntity<ApiResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
    userService.changePassword(dto);
    return ApiResponse.success("Password changed successfully", HttpStatus.OK, null);
  }

  @DeleteMapping("/delete")
  @Operation(summary = "Delete account", description = "Delete the currently authenticated user's account")
  public ResponseEntity<ApiResponse<Object>> deleteAccount() {
    userService.deleteCurrentUser();
    return ApiResponse.success("Account deleted successfully", HttpStatus.OK, null);
  }
}
