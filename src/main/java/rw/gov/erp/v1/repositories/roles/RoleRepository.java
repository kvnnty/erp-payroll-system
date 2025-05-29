package rw.gov.erp.v1.repositories.roles;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rw.gov.erp.v1.entities.employee.Role;
import rw.gov.erp.v1.enums.employee.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByType(ERole type);

  boolean existsByType(ERole type);
}