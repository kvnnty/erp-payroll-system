package rw.gov.erp.v1.controllers.admin;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.employee.CreateEmployeeDto;
import rw.gov.erp.v1.payload.ApiResponse;
import rw.gov.erp.v1.services.admin.AdminService;

@RestController
@RequestMapping("/api/v1/admin/managers")
@Tag(name = "Admin Resource", description = "APIs allowing System admin to manage system managers")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ApiResponse<Object>> createManager(@Valid @RequestBody CreateEmployeeDto createManangerDTO) {
    adminService.registerManager(createManangerDTO);
    return ApiResponse.success("Manager created successfully", HttpStatus.CREATED, null);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{managerId}")
  public ResponseEntity<ApiResponse<Object>> deleteManager(@PathVariable UUID managerId) {
    adminService.deleteManager(managerId);
    return ApiResponse.success("Manager deleted successfully", HttpStatus.OK, null);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<ApiResponse<Object>> getAllAdmins() {
    var employees = adminService.getAllManagers();
    return ApiResponse.success("All Managers fetched", HttpStatus.OK, employees);
  }

}
