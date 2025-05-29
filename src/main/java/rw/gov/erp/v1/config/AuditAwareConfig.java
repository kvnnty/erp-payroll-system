package rw.gov.erp.v1.config;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import rw.gov.erp.v1.security.user.UserPrincipal;

@Component("auditorAware")
public class AuditAwareConfig implements AuditorAware<UUID> {
  @Override
  public @NonNull Optional<UUID> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
      return Optional.empty();
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof UserPrincipal userPrincipal) {
      return Optional.ofNullable(userPrincipal.getId());
    } else {
      return Optional.empty();
    }
  }

}
