package rw.gov.erp.v1.repositories.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rw.gov.erp.v1.entities.payroll.Payslip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, UUID> {

  Optional<Payslip> findByEmployeeIdAndMonthAndYear(UUID employeeId, Integer month, Integer year);

  List<Payslip> findByMonthAndYear(Integer month, Integer year);

  List<Payslip> findByEmployeeId(UUID employeeId);

  @Query("SELECT p FROM Payslip p WHERE p.employee.id = :employeeId ORDER BY p.year DESC, p.month DESC")
  List<Payslip> findByEmployeeIdOrderByYearDescMonthDesc(@Param("employeeId") UUID employeeId);

  boolean existsByEmployeeIdAndMonthAndYear(UUID employeeId, Integer month, Integer year);
}
