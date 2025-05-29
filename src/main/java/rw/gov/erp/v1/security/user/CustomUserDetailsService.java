package rw.gov.erp.v1.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.repositories.user.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final EmployeeRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Employee user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    return new UserPrincipal(user);
  }

}