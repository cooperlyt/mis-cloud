package io.github.cooperlyt.commons.cloud.keycloak.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class ReactiveKeycloakSecurityContextHolder {

    public interface JwtUserConverter<T extends UserInfo> {

        T converter(Jwt principal);
    }

    public static class JwtBaseUserConverter implements JwtUserConverter<UserInfo>{

        @Override
        public UserInfo converter(Jwt principal) {
            return UserInfo.builder()
                .id(principal.getClaimAsString("sub"))
                .username(principal.getClaimAsString("preferred_username"))
                .firstName(principal.getClaimAsString("given_name"))
                .lastName(principal.getClaimAsString("family_name")).build();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo{
        private String id;
        private String username;
        private String firstName;
        private String lastName;

        public String getName(){
            return Optional.ofNullable(lastName).filter(StringUtils::hasText).orElse("") +
                Optional.ofNullable(firstName).filter(StringUtils::hasText).orElse("");
        }
    }

    @Getter
    public static class KeycloakUserSecurityContext<T extends UserInfo> extends KeycloakSecurityContext{

        public KeycloakUserSecurityContext(SecurityContext origin, T userInfo) {
            super(origin);
            this.userInfo = userInfo;
        }

        private final T userInfo;

    }

    public static Mono<KeycloakUserSecurityContext<UserInfo>> getContext(){
        return getContext(new JwtBaseUserConverter());
    }

    public static <T extends UserInfo> Mono<KeycloakUserSecurityContext<T>> getContext(JwtUserConverter<T> converter){
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
                .flatMap(context -> {
                    if (!context.getAuthentication().isAuthenticated()){
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                    }
                    Object principal = context.getAuthentication().getPrincipal();
                    if (principal instanceof Jwt){
//                        JwtUserConverter<UserInfo> converter = new JwtBaseUserConverter();
                        return Mono.just(new KeycloakUserSecurityContext<>(context,converter.converter((Jwt) principal)));
                    }else{
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                    }

                });
    }
}
