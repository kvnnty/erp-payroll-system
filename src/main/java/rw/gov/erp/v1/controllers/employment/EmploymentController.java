package rw.gov.erp.v1.controllers.employment;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.employment.EmploymentRequestDto;
import rw.gov.erp.v1.dtos.responses.employment.EmploymentResponseDto;
import rw.gov.erp.v1.payload.ApiResponse;
import rw.gov.erp.v1.services.employment.EmploymentService;

@RestController
@RequestMapping("/api/v1/employments")
@RequiredArgsConstructor
@Tag(name = "Employment Management", description = "Employment management APIs")
@SecurityRequirement(name = "bearerAuth")
public class EmploymentController {

    private final EmploymentService employmentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get all employments", description = "Retrieve all employments (Admin/Manager only)")
    public ResponseEntity<ApiResponse<List<EmploymentResponseDto>>> getAllEmployments() {
        return ApiResponse.success("All Employments retrieved successfully", employmentService.getAllEmployments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get employment by ID", description = "Retrieve employment by ID (Admin/Manager only)")
    public ResponseEntity<ApiResponse<EmploymentResponseDto>> getEmploymentById(@PathVariable UUID id) {
        return ApiResponse.success("Employment retrieved", employmentService.getEmploymentById(id));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('EMPLOYEE') and #employeeId == authentication.principal.id)")
    @Operation(summary = "Get employment by employee ID", description = "Retrieve employment by employee ID")
    public ResponseEntity<ApiResponse<EmploymentResponseDto>> getEmploymentByEmployeeId(@PathVariable UUID employeeId) {
        return ApiResponse.success("Employee\'s emploment retrieved",
                employmentService.getEmploymentByEmployeeId(employeeId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Create employment", description = "Create a new employment (Admin/Manager only)")
    public ResponseEntity<ApiResponse<EmploymentResponseDto>> createEmployment(
            @Valid @RequestBody EmploymentRequestDto request) {
        EmploymentResponseDto employment = employmentService.createEmployment(request);
        return ApiResponse.success("Employment created successfully", HttpStatus.CREATED, employment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update employment", description = "Update employment (Admin/Manager only)")
    public ResponseEntity<ApiResponse<EmploymentResponseDto>> updateEmployment(@PathVariable UUID id,
            @Valid @RequestBody EmploymentRequestDto request) {
        EmploymentResponseDto employment = employmentService.updateEmployment(id, request);
        return ApiResponse.success("Employment updated successfully", employment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete employment", description = "Delete employment (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteEmployment(@PathVariable UUID id) {
        employmentService.deleteEmployment(id);
        return ApiResponse.success("Employment deleted", HttpStatus.NO_CONTENT, null);
    }
}
