package rw.gov.erp.v1.services.admin.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rw.gov.erp.v1.dtos.requests.employee.CreateEmployeeDto;
import rw.gov.erp.v1.dtos.responses.user.UserResponseDTO;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.entities.employee.Role;
import rw.gov.erp.v1.enums.employee.ERole;
import rw.gov.erp.v1.enums.employee.EmployeeStatus;
import rw.gov.erp.v1.exceptions.DuplicateResourceException;
import rw.gov.erp.v1.exceptions.ResourceNotFoundException;
import rw.gov.erp.v1.repositories.roles.RoleRepository;
import rw.gov.erp.v1.repositories.user.EmployeeRepository;
import rw.gov.erp.v1.services.admin.AdminService;
import rw.gov.erp.v1.utils.mappers.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final EmployeeRepository employeeRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void registerManager(CreateEmployeeDto requestDto) {
    if (employeeRepository.existsByEmail(requestDto.getEmail())) {
      throw new DuplicateResourceException("Email is already registered");
    }
    if (employeeRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
      throw new DuplicateResourceException("Phone number is already registered");
    }

    Role managerRole = roleRepository.findByType(ERole.ROLE_MANAGER)
        .orElseThrow(() -> new IllegalStateException("Manager role not found"));

    Employee user = Employee.builder()
        .code("MANAGER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
        .firstName(requestDto.getFirstName())
        .lastName(requestDto.getLastName())
        .email(requestDto.getEmail())
        .password(passwordEncoder.encode(requestDto.getPassword()))
        .phoneNumber(requestDto.getPhoneNumber())
        .dateOfBirth(requestDto.getDateOfBirth())
        .status(EmployeeStatus.ACTIVE)
        .role(managerRole)
        .build();

    employeeRepository.save(user);
  }

  @Override
  public void deleteManager(UUID adminId) {
    Employee user = employeeRepository.findById(adminId)
        .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

    if (!user.getRole().getType().equals(ERole.ROLE_MANAGER)) {
      throw new IllegalArgumentException("User is not an admin");
    }

    employeeRepository.delete(user);
  }

  @Override
  public List<UserResponseDTO> getAllManagers() {
    Role adminRole = roleRepository.findByType(ERole.ROLE_MANAGER)
        .orElseThrow(() -> new IllegalStateException("Admin role not found"));

    List<Employee> admins = employeeRepository.findAllByRole(adminRole);
    return admins.stream()
        .map(UserMapper::toDto)
        .toList();
  }

}
