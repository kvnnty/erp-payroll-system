package rw.gov.erp.v1.services.employee.impl;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.employee.ChangePasswordDto;
import rw.gov.erp.v1.dtos.requests.employee.CreateEmployeeDto;
import rw.gov.erp.v1.dtos.requests.employee.UpdateEmployeeDto;
import rw.gov.erp.v1.dtos.responses.user.UserResponseDTO;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.entities.employee.Role;
import rw.gov.erp.v1.enums.employee.ERole;
import rw.gov.erp.v1.enums.employee.EmployeeStatus;
import rw.gov.erp.v1.exceptions.BadRequestException;
import rw.gov.erp.v1.exceptions.DuplicateResourceException;
import rw.gov.erp.v1.exceptions.ResourceNotFoundException;
import rw.gov.erp.v1.repositories.roles.RoleRepository;
import rw.gov.erp.v1.repositories.user.EmployeeRepository;
import rw.gov.erp.v1.security.user.UserPrincipal;
import rw.gov.erp.v1.services.employee.EmployeeService;
import rw.gov.erp.v1.utils.mappers.UserMapper;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void registerUser(CreateEmployeeDto requestDto) {
    if (employeeRepository.existsByEmail(requestDto.getEmail())) {
      throw new DuplicateResourceException("Email is already in use.");
    }
    if (employeeRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
      throw new DuplicateResourceException("Phone number is already in use.");
    }

    Role employeeRole = roleRepository.findByType(ERole.ROLE_EMPLOYEE)
        .orElseThrow(() -> new IllegalStateException("Role not found"));

    Employee newCustomer = Employee.builder()
        .code("EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
        .firstName(requestDto.getFirstName())
        .lastName(requestDto.getLastName())
        .email(requestDto.getEmail())
        .password(passwordEncoder.encode(requestDto.getPassword()))
        .phoneNumber(requestDto.getPhoneNumber())
        .dateOfBirth(requestDto.getDateOfBirth())
        .status(EmployeeStatus.ACTIVE)
        .role(employeeRole)
        .build();

    employeeRepository.save(newCustomer);

  }

  @Override
  public Employee getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getPrincipal() == null) {
      throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
    }

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Employee user = employeeRepository.findByEmail(userPrincipal.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return user;
  }

  @Override
  @Transactional
  public void updateEmployeeProfile(UpdateEmployeeDto requestDto) {
    Employee employee = getCurrentUser();

    if (requestDto.getPhoneNumber().equals(employee.getPhoneNumber())) {
      throw new BadRequestException("The new phone number cannot be the same as your current one.");
    }
    if (requestDto.getEmail().equals(employee.getEmail())) {
      throw new BadRequestException("The new email cannot be the same as your current one.");
    }

    if (employeeRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
      throw new DuplicateResourceException("Phone number is already in use.");
    }
    if (employeeRepository.existsByEmail(requestDto.getEmail())) {
      throw new DuplicateResourceException("Email is already in use.");
    }

    employee.setFirstName(requestDto.getFirstName());
    employee.setLastName(requestDto.getLastName());
    employee.setPhoneNumber(requestDto.getPhoneNumber());
    employee.setEmail(requestDto.getEmail());
    employee.setDateOfBirth(requestDto.getDateOfBirth());

    employeeRepository.save(employee);
  }

  @Override
  public void changePassword(ChangePasswordDto dto) {
    Employee user = getCurrentUser();

    if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
      throw new BadCredentialsException("Current password is incorrect.");
    }

    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    employeeRepository.save(user);
  }

  @Override
  @Transactional
  public void deleteCurrentUser() {
    Employee user = getCurrentUser();
    employeeRepository.delete(user);
  }

  @Override
  public UserResponseDTO getMyprofile() {
    Employee user = this.getCurrentUser();
    return UserMapper.toDto(user);
  }

  @Override
  public Employee getEmployeeById(UUID id) {
    return employeeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
  }

  @Override
  public Employee getEmployeeByEmail(String email) {
    return employeeRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
  }
}
