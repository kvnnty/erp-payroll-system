package rw.gov.erp.v1.services.employment;

import java.util.List;
import java.util.UUID;

import rw.gov.erp.v1.dtos.requests.employment.EmploymentRequestDto;
import rw.gov.erp.v1.dtos.responses.employment.EmploymentResponseDto;

public interface EmploymentService {

    public List<EmploymentResponseDto> getAllEmployments();

    public EmploymentResponseDto getEmploymentById(UUID id);

    public EmploymentResponseDto getEmploymentByEmployeeId(UUID employeeId);

    public EmploymentResponseDto createEmployment(EmploymentRequestDto request);

    public EmploymentResponseDto updateEmployment(UUID id, EmploymentRequestDto request);

    public void deleteEmployment(UUID id);
}
