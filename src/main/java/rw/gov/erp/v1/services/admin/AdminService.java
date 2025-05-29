package rw.gov.erp.v1.services.admin;

import java.util.List;
import java.util.UUID;

import rw.gov.erp.v1.dtos.requests.employee.CreateEmployeeDto;
import rw.gov.erp.v1.dtos.responses.user.UserResponseDTO;

public interface AdminService {
  void registerManager(CreateEmployeeDto requestDto);

  void deleteManager(UUID adminId);

  List<UserResponseDTO> getAllManagers();
}
