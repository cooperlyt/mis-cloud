package io.github.cooperlyt.mis.service.work;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cooperlyt.commons.cloud.keycloak.auth.ReactiveKeycloakAuthenticationConverter;
import io.github.cooperlyt.commons.cloud.serialize.IntegerToBooleanConverter;
import io.github.cooperlyt.commons.data.StringListReadingConverter;
import io.github.cooperlyt.commons.data.StringListWritingConverter;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.List;

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

            .pathMatchers("/protected/**").hasRole("default-roles-construction")

            .pathMatchers("/protected/gov/**").hasRole("gov-sale-record")

            .pathMatchers("/protected/corp/**").hasRole("corp-joint")

            .pathMatchers("/protected/corp/developer/**").hasRole("corp-developer-sale")

            .anyExchange().authenticated()
        )
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
            .jwtAuthenticationConverter(new ReactiveKeycloakAuthenticationConverter())));

    return http.build();
  }


  @Bean
  public R2dbcCustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<>();


    converters.add(new IntegerToBooleanConverter());
    converters.add(new StringListReadingConverter());
    converters.add(new StringListWritingConverter());


    return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, converters);
    // deprecated: return new R2dbcCustomConversions(converters);
  }


  @Bean
  public OpenAPI restfulOpenAPI(){
    return new OpenAPI()
        .info(new Info().title("操作记录")
            .description("操作记录，包括业务和非业务")
            .version("v0.0.1"))
        .externalDocs(new ExternalDocumentation()
            .description("SpringDoc Wiki Documentation")
            .url("https://springdoc.org/v2"));
  }
}
