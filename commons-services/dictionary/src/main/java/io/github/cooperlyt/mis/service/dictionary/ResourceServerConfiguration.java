package io.github.cooperlyt.mis.service.dictionary;

import io.github.cooperlyt.commons.cloud.keycloak.ReactiveKeycloakAuthenticationConverter;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class ResourceServerConfiguration {

  private static final String[] AUTH_LIST = {
      // -- swagger ui
      "/swagger-ui.html",
      "/swagger-ui/*",
      "/swagger-resources/**",
      "/v2/api-docs",
      "/v3/api-docs",
      "/webjars/**",


      "/actuator/**",
      "/public/**",
      "/internal/**"
  };

  @Bean
  SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
    http
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers(AUTH_LIST).permitAll()

            .pathMatchers("/protected/**").hasRole("system-configure-manager")


            .anyExchange().authenticated()
        )
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
            .jwtAuthenticationConverter(new ReactiveKeycloakAuthenticationConverter())));
    return http.build();
  }




  @Bean
  public OpenAPI restfulOpenAPI(){
    return new OpenAPI()
        .info(new Info().title("字典服务接口")
            .description("字典和行政区划服务")
            .version("v0.0.1"))
        .externalDocs(new ExternalDocumentation()
            .description("SpringDoc Wiki Documentation")
            .url("https://springdoc.org/v2"));
  }

}
