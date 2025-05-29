package rw.gov.erp.v1.services.employee;

import java.util.UUID;

import rw.gov.erp.v1.dtos.requests.employee.ChangePasswordDto;
import rw.gov.erp.v1.dtos.requests.employee.CreateEmployeeDto;
import rw.gov.erp.v1.dtos.requests.employee.UpdateEmployeeDto;
import rw.gov.erp.v1.dtos.responses.user.UserResponseDTO;
import rw.gov.erp.v1.entities.employee.Employee;

public interface EmployeeService {
  void registerUser(CreateEmployeeDto requestDTO);

  UserResponseDTO getMyprofile();

  Employee getCurrentUser();

  void updateEmployeeProfile(UpdateEmployeeDto dto);

  void changePassword(ChangePasswordDto dto);

  void deleteCurrentUser();

  public Employee getEmployeeById(UUID id);

  public Employee getEmployeeByEmail(String email);
}
