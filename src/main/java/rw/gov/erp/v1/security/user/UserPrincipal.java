package rw.gov.erp.v1.security.user;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.enums.employee.ERole;
import rw.gov.erp.v1.enums.employee.EmployeeStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {
  private UUID id;
  private String email;
  private String password;
  private EmployeeStatus status;
  private Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(Employee user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.status = user.getStatus();
    this.authorities = List.of(
        new SimpleGrantedAuthority(
            user.getRole() != null ? user.getRole().getType().name() : ERole.ROLE_EMPLOYEE.name()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return status == EmployeeStatus.ACTIVE;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return status == EmployeeStatus.ACTIVE;
  }
}
