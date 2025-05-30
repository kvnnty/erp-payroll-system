package rw.gov.erp.v1.repositories.employment;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rw.gov.erp.v1.entities.employment.Employment;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, UUID> {

  Optional<Employment> findByCode(String code);

  Optional<Employment> findByEmployeeId(UUID employeeId);

  boolean existsByCode(String code);
}
