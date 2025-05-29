package rw.gov.erp.v1.seed;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.entities.employee.Role;
import rw.gov.erp.v1.enums.employee.ERole;
import rw.gov.erp.v1.enums.employee.EmployeeStatus;
import rw.gov.erp.v1.repositories.roles.RoleRepository;
import rw.gov.erp.v1.repositories.user.EmployeeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final RoleRepository roleRepository;
  private final EmployeeRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${application.admin.email}")
  private String email;

  @Value("${application.admin.first-name}")
  private String firstName;

  @Value("${application.admin.last-name}")
  private String lastName;

  @Value("${application.admin.password}")
  private String password;

  @Value("${application.admin.phoneNumber}")
  private String phoneNumber;

  @Value("${application.admin.dob}")
  private LocalDate dateOfBirth;

  @Override
  public void run(ApplicationArguments args) {
    seedRoles();
    seedAdmin();
  }

  private void seedRoles() {
    Arrays.stream(ERole.values()).forEach(roleEnum -> {
      if (!roleRepository.existsByType(roleEnum)) {
        roleRepository.save(Role.builder().type(roleEnum).build());
      }
    });
  }

  private void seedAdmin() {
    if (userRepository.findByEmail(email).isEmpty()) {
      Role adminRole = roleRepository.findByType(ERole.ROLE_ADMIN)
          .orElseThrow(() -> new RuntimeException("Admin role not found"));

      Employee adminUser = Employee.builder()
          .code("ADMIN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
          .email(email)
          .firstName(firstName)
          .lastName(lastName)
          .password(passwordEncoder.encode(password))
          .phoneNumber(phoneNumber)
          .dateOfBirth(dateOfBirth)
          .status(EmployeeStatus.ACTIVE)
          .role(adminRole)
          .build();

      userRepository.save(adminUser);

      log.info("System admin created with email: {}", email);

    }
  }
}
