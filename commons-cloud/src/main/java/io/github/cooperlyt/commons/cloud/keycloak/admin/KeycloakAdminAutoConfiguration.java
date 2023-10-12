package io.github.cooperlyt.commons.cloud.keycloak.admin;


import io.github.cooperlyt.commons.cloud.resteasy.ResteasyClientAutoConfiguration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@AutoConfiguration(after = ResteasyClientAutoConfiguration.class)
@ConditionalOnClass(Keycloak.class)
public class KeycloakAdminAutoConfiguration {

  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass(KeycloakBuilder.class)
  @ConditionalOnProperty(prefix ="keycloak.admin", name = "serverUrl")
  @EnableConfigurationProperties(KeycloakAdminProperties.class)
  static class KeycloakAdminClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KeycloakBuilder keycloakAdminClientBuilder(KeycloakAdminProperties properties) {
      return KeycloakBuilder.builder()
          .serverUrl(properties.getServerUrl())
          .realm(properties.getRealm())
          .clientId(properties.getClientId())
          .grantType(properties.getGrantType())
//          .resteasyClient(properties.getResteasyClient())
          .authorization(properties.getAuthorization())
          .scope(properties.getScope())
          .username(properties.getCredentials().getUsername())
          .password(properties.getCredentials().getPassword())
          .clientSecret(properties.getCredentials().getClientSecret());
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    @ConditionalOnBean(ResteasyClient.class)
    public Keycloak keycloakAdminClientOnResteasyClient(KeycloakBuilder builder, ResteasyClient resteasyClient) {
      return builder.resteasyClient(resteasyClient).build();
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean({Keycloak.class,ResteasyClient.class})
    @ConditionalOnBean(ResteasyClientBuilder.class)
    public Keycloak keycloakAdminClientOnResteasyBuilder(KeycloakBuilder builder, ResteasyClientBuilder resteasyClientBuilder) {
      return builder.resteasyClient(resteasyClientBuilder.build()).build();
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean({Keycloak.class,ResteasyClient.class,ResteasyClientBuilder.class})
    public Keycloak keycloakAdminClient(KeycloakBuilder builder) {
      return builder.build();
    }

  }


}
