package rw.gov.erp.v1.repositories.payroll;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rw.gov.erp.v1.entities.deduction.Deduction;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, UUID> {

  Optional<Deduction> findByCode(String code);

  Optional<Deduction> findByDeductionName(String deductionName);

  boolean existsByCode(String code);

  boolean existsByDeductionName(String deductionName);
}