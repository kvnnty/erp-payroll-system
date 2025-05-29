package rw.gov.erp.v1.controllers.deductions;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.deduction.DeductionRequestDto;
import rw.gov.erp.v1.entities.deduction.Deduction;
import rw.gov.erp.v1.payload.ApiResponse;
import rw.gov.erp.v1.services.deduction.DeductionService;

@RestController
@RequestMapping("/api/v1/deductions")
@RequiredArgsConstructor
@Tag(name = "Deduction Management", description = "Deduction management APIs")
@SecurityRequirement(name = "bearerAuth")
public class DeductionController {

  private final DeductionService deductionService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  @Operation(summary = "Get all deductions", description = "Retrieve all deductions (Admin/Manager only)")
  public ResponseEntity<ApiResponse<List<Deduction>>> getAllDeductions() {
    return ApiResponse.success("All deductions retrieved successfully", deductionService.getAllDeductions());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
  @Operation(summary = "Get deduction by ID", description = "Retrieve deduction by ID (Admin/Manager only)")
  public ResponseEntity<ApiResponse<Deduction>> getDeductionById(@PathVariable UUID id) {
    return ApiResponse.success("Deduction retrieved successfully", deductionService.getDeductionById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create deduction", description = "Create a new deduction (Admin only)")
  public ResponseEntity<ApiResponse<Deduction>> createDeduction(@Valid @RequestBody DeductionRequestDto request) {
    Deduction deduction = deductionService.createDeduction(request);
    return ApiResponse.success("Deduction created successfully", HttpStatus.CREATED, deduction);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update deduction", description = "Update deduction (Admin only)")
  public ResponseEntity<ApiResponse<Deduction>> updateDeduction(@PathVariable UUID id,
      @Valid @RequestBody DeductionRequestDto request) {
    Deduction deduction = deductionService.updateDeduction(id, request);
    return ApiResponse.success("Deduction updated successfully", deduction);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete deduction", description = "Delete deduction (Admin only)")
  public ResponseEntity<ApiResponse<Void>> deleteDeduction(@PathVariable UUID id) {
    deductionService.deleteDeduction(id);
    return ApiResponse.success("Deduction deleted successfully", HttpStatus.NO_CONTENT, null);
  }
}
