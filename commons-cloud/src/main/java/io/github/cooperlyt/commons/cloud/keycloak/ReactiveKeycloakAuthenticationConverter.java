package io.github.cooperlyt.commons.cloud.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ReactiveKeycloakAuthenticationConverter
        implements Converter<Jwt, Mono<AbstractAuthenticationToken>>{

  private static final String ROLE_PREFIX = "ROLE_";
  private static final String SCOPE_PREFIX = "SCOPE_";

  private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  protected Collection<GrantedAuthority> authenticationRoles(Jwt jwt) {

    log.info("convert jwt to spring role");

    log.debug("Claims:");
    log.debug(jwt.getClaims().toString());
    log.debug("Header:");
    log.debug(jwt.getHeaders().toString());
    log.debug("token:");
    log.debug(jwt.getTokenValue());

    // keycloak 14 default jwt  format
//Claims
//    {sub=d99df9f7-cfeb-4e4c-98f8-1f3f2fc5305f, resource_access={"account":{"roles":["manage-account","manage-account-links","view-profile"]}}, email_verified=false, allowed-origins=["*"], iss=http://localhost:8901/auth/realms/house-sale, typ=Bearer, preferred_username=test, given_name=李, locale=zh-CN, nonce=01404690-f88d-4933-80b2-d54c1e2780bd, aud=[account], acr=1, realm_access={"roles":["default-roles-house-sale","offline_access","uma_authorization"]}, azp=web, auth_time=1658370401, scope=openid profile email, name=李 迎涛, exp=2022-07-21T02:31:41Z, session_state=0fc3ffb1-cba3-4f26-b831-c3907042fef4, iat=2022-07-21T02:26:41Z, family_name=迎涛, jti=52491e11-396a-43b8-bf36-88fd7898a790, email=cooper.lee.g@gmail.com}
//Header
//    {kid=pjoGSTdw82AvUNOfJkSsIz5OXcjQiamvz4it554y-oI, typ=JWT, alg=RS256}
//token
//    eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJwam9HU1RkdzgyQXZVTk9mSmtTc0l6NU9YY2pRaWFtdno0aXQ1NTR5LW9JIn0.eyJleHAiOjE2NTgzNzA3MDEsImlhdCI6MTY1ODM3MDQwMSwiYXV0aF90aW1lIjoxNjU4MzcwNDAxLCJqdGkiOiI1MjQ5MWUxMS0zOTZhLTQzYjgtYmYzNi04OGZkNzg5OGE3OTAiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojg5MDEvYXV0aC9yZWFsbXMvaG91c2Utc2FsZSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJkOTlkZjlmNy1jZmViLTRlNGMtOThmOC0xZjNmMmZjNTMwNWYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3ZWIiLCJub25jZSI6IjAxNDA0NjkwLWY4OGQtNDkzMy04MGIyLWQ1NGMxZTI3ODBiZCIsInNlc3Npb25fc3RhdGUiOiIwZmMzZmZiMS1jYmEzLTRmMjYtYjgzMS1jMzkwNzA0MmZlZjQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtaG91c2Utc2FsZSIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoi5p2OIOi_jua2myIsInByZWZlcnJlZF91c2VybmFtZSI6InRlc3QiLCJnaXZlbl9uYW1lIjoi5p2OIiwibG9jYWxlIjoiemgtQ04iLCJmYW1pbHlfbmFtZSI6Iui_jua2myIsImVtYWlsIjoiY29vcGVyLmxlZS5nQGdtYWlsLmNvbSJ9.xIGTkR5hfJfUcgBDxmAGBgZMD3hpMEmxgxy2koPEzpCcIKaE5BxitSV2_OygwYjkAUtfQrc07x6jFQ0XRkjHk-NtNnGLaYx0hPLe-BRuE4Qq8X7lo28yWweut4OSHjUagB2HI2rQko0d3WjmqaXCaS0E-Gjreh5lQwAhFiNagA6AJ1AbsUL8VwOiH8ARTkQAr2qQxb-T_ciPY33-QPwOEep5tXEriB0ruHsqVYrHapZxOCJreFfgH66Z-NY8tqfLC25NJx_WeA-2ClBtVzGTe9BVw1mvNx3uNnnpXpUNIvxaFUKrVqTihqjKU1DAdNWK7v9lLfw0uwcu40G3j5p-LQ

    Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
    assert authorities != null;
    //final String clientId = jwt.getClaim("azp");
    final Map<String, Map<String,Collection<String>>> resourceAccess = jwt.getClaim("resource_access");
    if (resourceAccess != null){
      resourceAccess.forEach((k,v) -> authorities
          .addAll(v.get("roles").stream().map(x -> new SimpleGrantedAuthority(ROLE_PREFIX + k + "_" + x ))
              .collect(Collectors.toSet())));
    }

//    Map<String,Collection<String>> resource;
//    Collection<String> resourceRoles;
//    if (resourceAccess != null &&
//            clientId != null &&
//            (resource = resourceAccess.get(clientId)) != null &&
//            (resourceRoles = resource.get("roles")) != null) {
//      authorities.addAll(resourceRoles.stream().map((x) -> new SimpleGrantedAuthority("ROLE_" + x)).collect(Collectors.toSet()));
//    }

    Map<String,Collection<String>> realmAccess = jwt.getClaim("realm_access");
    Collection<String> realmRoles;
    if (realmAccess != null && (realmRoles = realmAccess.get("roles")) != null){
      authorities.addAll(realmRoles.stream().map((x) -> new SimpleGrantedAuthority(ROLE_PREFIX + x)).collect(Collectors.toSet()));
    }

    final String scope = jwt.getClaim("scope");
    if (StringUtils.hasText(scope)){
      authorities.addAll(Arrays.stream(scope.split("\\s+")).map(s -> new SimpleGrantedAuthority(SCOPE_PREFIX + s)).collect(Collectors.toSet()));
    }

    return authorities;
  }

  @Override
  public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
    return Mono.just(new JwtAuthenticationToken(jwt, this.authenticationRoles(jwt))) ;
  }
}
