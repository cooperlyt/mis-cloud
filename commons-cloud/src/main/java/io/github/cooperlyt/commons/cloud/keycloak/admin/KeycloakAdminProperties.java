package io.github.cooperlyt.commons.cloud.keycloak.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakAdminProperties {

  private String serverUrl;
  private String realm;
  private String clientId;
  private String grantType;
  //    private Client resteasyClient;
  private String authorization;
  private String scope;

  private ClientCredentialsProperties credentials;

  @Getter
  @Setter
  static class ClientCredentialsProperties {
    private String clientSecret;
    private String username;
    private String password;
  }
}
