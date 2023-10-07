package io.github.cooperlyt.commons.cloud.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter
            = new JwtGrantedAuthoritiesConverter();

    private String clientId = null;

    public KeycloakAuthenticationConverter(String clientId) {
        this.clientId = clientId;
    }

    public KeycloakAuthenticationConverter() {
    }

    @SuppressWarnings("unchecked")
    protected Collection<GrantedAuthority> authenticationRoles(Jwt jwt){

        if (clientId == null){
            clientId = jwt.getClaim("azp");
        }


        Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (resourceAccess != null && clientId != null &&
                (resource = (Map<String, Object>) resourceAccess.get(clientId)) != null
                && (resourceRoles = (Collection<String>) resource.get("roles")) != null) {
            assert authorities != null;
            authorities.addAll(resourceRoles.stream()
                    .map(x -> new SimpleGrantedAuthority("ROLE_" + x))
                    .collect(Collectors.toSet()));
        }
        return authorities;
    }


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return new JwtAuthenticationToken(jwt, authenticationRoles(jwt));
    }
}
