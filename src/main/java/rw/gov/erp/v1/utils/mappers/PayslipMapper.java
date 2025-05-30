package rw.gov.erp.v1.utils.mappers;

import rw.gov.erp.v1.dtos.responses.payslip.PayslipResponseDto;
import rw.gov.erp.v1.entities.payroll.Payslip;

public class PayslipMapper {
  public static PayslipResponseDto toDto(Payslip payslip) {
    return PayslipResponseDto.builder()
        .employeeId(payslip.getId())
        .employeeId(payslip.getEmployee().getId())
        .houseAmount(payslip.getHouseAmount())
        .transportAmount(payslip.getTransportAmount())
        .employeeTaxedAmount(payslip.getEmployeeTaxedAmount())
        .pensionAmount(payslip.getPensionAmount())
        .medicalInsuranceAmount(payslip.getMedicalInsuranceAmount())
        .otherTaxedAmount(payslip.getOtherTaxedAmount())
        .grossSalary(payslip.getGrossSalary())
        .netSalary(payslip.getNetSalary())
        .baseSalary(payslip.getBaseSalary())
        .month(payslip.getMonth())
        .year(payslip.getYear())
        .build();
  }
}
