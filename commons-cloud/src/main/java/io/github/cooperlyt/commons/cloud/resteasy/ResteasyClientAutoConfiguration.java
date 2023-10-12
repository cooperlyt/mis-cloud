package io.github.cooperlyt.commons.cloud.resteasy;

import io.github.cooperlyt.commons.cloud.keycloak.admin.KeycloakAdminProperties;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Optional;

@AutoConfiguration
@ConditionalOnClass(ResteasyClient.class)
public class ResteasyClientAutoConfiguration {


  @Configuration(proxyBeanMethods = false)
  @ConditionalOnClass(ResteasyClientBuilder.class)
  @ConditionalOnProperty(prefix ="resteasy", name = "client")
  @EnableConfigurationProperties(ResteasyClientProperties.class)
  static class SingleKeycloakAdminClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ResteasyClientBuilder resteasyClientBuilder(ResteasyClientProperties properties) {
      ResteasyClientBuilder result = new ResteasyClientBuilderImpl();
      Optional.of(properties.getConnection().getPoolSize()).ifPresent(result::connectionPoolSize);
      Optional.of(properties.getConnection().getCheckoutTimeout()).ifPresent(durationProperties -> result.connectionCheckoutTimeout(durationProperties.getDuration(), durationProperties.getUnit()));
      Optional.of(properties.getConnection().getMaxPooledPerRoute()).ifPresent(result::maxPooledPerRoute);
      Optional.of(properties.getDisableTrustManager()).filter(Boolean.TRUE::equals).ifPresent(disabled -> result.disableTrustManager());
      Optional.of(properties.getHostnameVerificationPolicy()).ifPresent(result::hostnameVerification);
      Optional.of(properties.getConnection().getTtl()).ifPresent(durationProperties -> result.connectionTTL(durationProperties.getDuration(), durationProperties.getUnit()));
      return result;
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public ResteasyClient resteasyClient(ResteasyClientBuilder builder) {
      return builder.build();
    }

  }

}
