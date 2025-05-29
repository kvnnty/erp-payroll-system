package rw.gov.erp.v1.repositories.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.entities.employee.Role;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
  Optional<Employee> findByEmail(String email);

  Optional<Employee> findByCode(String code);

  Optional<Employee> findByPhoneNumber(String phoneNumber);

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

  boolean existsByCode(String code);

  List<Employee> findAllByRole(Role role);

  @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE' AND e.employment.status = 'ACTIVE'")
  List<Employee> findAllActiveEmployees();
}
