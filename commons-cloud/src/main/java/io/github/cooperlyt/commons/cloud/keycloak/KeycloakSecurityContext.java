package io.github.cooperlyt.commons.cloud.keycloak;

import lombok.Getter;
import org.springframework.security.core.context.SecurityContext;

@Getter
public abstract class KeycloakSecurityContext {

  private final SecurityContext origin;

  public KeycloakSecurityContext(SecurityContext origin) {
    this.origin = origin;
  }

  public boolean isHasRole(String role) {
    return origin.getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
  }

}
