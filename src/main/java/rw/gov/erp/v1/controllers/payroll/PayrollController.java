package rw.gov.erp.v1.controllers.payroll;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.payroll.PayrollRequestDto;
import rw.gov.erp.v1.dtos.responses.payslip.PayslipResponseDto;
import rw.gov.erp.v1.payload.ApiResponse;
import rw.gov.erp.v1.security.user.UserPrincipal;
import rw.gov.erp.v1.services.payroll.PayrollService;

@RestController
@RequestMapping("/api/v1/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll Management", description = "Payroll management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Generate payroll", description = "Generate payroll for all active employees (Manager only)")
    public ResponseEntity<ApiResponse<Object>> generatePayroll(@Valid @RequestBody PayrollRequestDto request) {
        payrollService.generatePayroll(request);
        return ApiResponse.success(
                "Monthly Payroll for all employees generated successfully, sent to admin for approval",
                HttpStatus.CREATED,
                null);
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve payroll", description = "Approve payroll for a specific month/year (Admin only)")
    public ResponseEntity<ApiResponse<Object>> approvePayroll(@Valid @RequestBody PayrollRequestDto request) {
        payrollService.approvePayroll(request);
        return ApiResponse.success("Payroll approved successfully", null);
    }

    @GetMapping("/payslips")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get payslips by month/year", description = "Get all payslips for a specific month/year (Admin/Manager only)")
    public ResponseEntity<ApiResponse<List<PayslipResponseDto>>> getPayslipsByMonthAndYear(@RequestParam Integer month,
            @RequestParam Integer year) {
        List<PayslipResponseDto> payslips = payrollService.getPayslipsByMonthAndYear(month, year);
        return ApiResponse.success("All Payslips retrived successfully", payslips);
    }

    @GetMapping("/payslips/me")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Get my payslips", description = "Get current user's payslips")
    public ResponseEntity<ApiResponse<List<PayslipResponseDto>>> getMyPayslips(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<PayslipResponseDto> payslips = payrollService.getEmployeePayslips(userPrincipal.getId());
        return ApiResponse.success("All my payslips retrived successfully", payslips);
    }

    @GetMapping("/payslips/me/by-month-and-year")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "Get my payslip for specific month/year", description = "Get current user's payslip for specific month and year")
    public ResponseEntity<ApiResponse<PayslipResponseDto>> getMyPayslip(
            @RequestParam Integer month,
            @RequestParam Integer year,
            Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        PayslipResponseDto payslip = payrollService.getEmployeePayslip(userPrincipal.getId(), month, year);
        return ApiResponse.success("Payslip retrieved by month and year", payslip);
    }

    @GetMapping("/payslips/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get employee payslips", description = "Get specific employee's payslips (Admin/Manager only)")
    public ResponseEntity<ApiResponse<List<PayslipResponseDto>>> getEmployeePayslips(@PathVariable UUID employeeId) {
        List<PayslipResponseDto> payslips = payrollService.getEmployeePayslips(employeeId);
        return ApiResponse.success("Payslips for employee retrived succesfully", payslips);
    }
}
