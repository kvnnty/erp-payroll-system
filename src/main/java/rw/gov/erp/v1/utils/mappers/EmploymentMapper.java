package rw.gov.erp.v1.utils.mappers;

import rw.gov.erp.v1.dtos.responses.employment.EmploymentResponseDto;
import rw.gov.erp.v1.entities.employment.Employment;

public class EmploymentMapper {
  public static EmploymentResponseDto toDto(Employment employment) {
    return EmploymentResponseDto.builder()
        .id(employment.getId())
        .code(employment.getCode())
        .employeeId(employment.getEmployee().getId())
        .department(employment.getDepartment())
        .position(employment.getPosition())
        .baseSalary(employment.getBaseSalary())
        .status(employment.getStatus())
        .joiningDate(employment.getJoiningDate())
        .build();
  }
}
