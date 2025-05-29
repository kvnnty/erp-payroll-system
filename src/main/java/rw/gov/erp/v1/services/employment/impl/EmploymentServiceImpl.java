package rw.gov.erp.v1.services.employment.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.employment.EmploymentRequestDto;
import rw.gov.erp.v1.dtos.responses.employment.EmploymentResponseDto;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.entities.employee.Employment;
import rw.gov.erp.v1.exceptions.DuplicateResourceException;
import rw.gov.erp.v1.exceptions.ResourceNotFoundException;
import rw.gov.erp.v1.repositories.user.EmploymentRepository;
import rw.gov.erp.v1.services.employee.EmployeeService;
import rw.gov.erp.v1.services.employment.EmploymentService;
import rw.gov.erp.v1.utils.mappers.EmploymentMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EmploymentServiceImpl implements EmploymentService {

    private final EmploymentRepository employmentRepository;
    private final EmployeeService employeeService;

    public List<EmploymentResponseDto> getAllEmployments() {
        return employmentRepository.findAll().stream().map(EmploymentMapper::toDto).toList();
    }

    public EmploymentResponseDto getEmploymentById(UUID id) {
        return EmploymentMapper.toDto(employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id)));
    }

    public EmploymentResponseDto getEmploymentByEmployeeId(UUID employeeId) {
        return EmploymentMapper.toDto(employmentRepository.findByEmployeeId(employeeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Employment not found for employee id: " + employeeId)));
    }

    public EmploymentResponseDto createEmployment(EmploymentRequestDto request) {
        Employee employee = employeeService.getEmployeeById(request.getEmployeeId());

        if (employmentRepository.findByEmployeeId(request.getEmployeeId()).isPresent()) {
            throw new DuplicateResourceException("Employment already exists for employee: " + employee.getFirstName()
                    + " " + employee.getLastName());
        }

        Employment employment = new Employment();
        employment.setCode("EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        employment.setEmployee(employee);
        employment.setDepartment(request.getDepartment());
        employment.setPosition(request.getPosition());
        employment.setBaseSalary(request.getBaseSalary());
        employment.setStatus(request.getStatus());
        employment.setJoiningDate(request.getJoiningDate());

        return EmploymentMapper.toDto(employmentRepository.save(employment));
    }

    public EmploymentResponseDto updateEmployment(UUID id, EmploymentRequestDto request) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id));

        Employee employee = employeeService.getEmployeeById(request.getEmployeeId());

        employment.setEmployee(employee);
        employment.setDepartment(request.getDepartment());
        employment.setPosition(request.getPosition());
        employment.setBaseSalary(request.getBaseSalary());
        employment.setStatus(request.getStatus());
        employment.setJoiningDate(request.getJoiningDate());

        return EmploymentMapper.toDto(employmentRepository.save(employment));
    }

    public void deleteEmployment(UUID id) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id));

        employmentRepository.delete(employment);
    }
}
