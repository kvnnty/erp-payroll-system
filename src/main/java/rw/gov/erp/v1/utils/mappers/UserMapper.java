package rw.gov.erp.v1.utils.mappers;

import rw.gov.erp.v1.dtos.responses.user.UserResponseDTO;
import rw.gov.erp.v1.entities.employee.Employee;

public class UserMapper {
  public static UserResponseDTO toDto(Employee user) {
    return UserResponseDTO.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .status(user.getStatus())
        .dateOfBirth(user.getDateOfBirth())
        .role(user.getRole().getType())
        .build();
  }
}
