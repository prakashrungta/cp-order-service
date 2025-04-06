package com.restaurant.orders.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converter class to extract roles from a Keycloak JWT token and convert them into Spring Security GrantedAuthority objects.
 * This class implements the Converter interface to convert a Jwt object into a collection of GrantedAuthority objects.
 */
public class KeycloakJwtTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    /**
     * Converts a Jwt object into a collection of GrantedAuthority objects.
     * Extracts roles from the "realm_access" and "resource_access" claims in the JWT token.
     *
     * @param jwt the JWT token to convert
     * @return a collection of GrantedAuthority objects representing the roles in the JWT token
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Convert default authorities using JwtGrantedAuthoritiesConverter
        Collection<GrantedAuthority> authorities = defaultGrantedAuthoritiesConverter.convert(jwt);

        // Extract roles from realm_access claim
        List<String> realmRoles = (List<String>) jwt.getClaim("realm_access.roles");
        if (realmRoles != null) {
            authorities.addAll(realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList()));
        }

        // Extract roles from resource_access claim
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.forEach((resource, access) -> {
                Map<String, List<String>> accessMap = (Map<String, List<String>>) access;
                List<String> resourceRoles = accessMap.get("roles");
                if (resourceRoles != null) {
                    authorities.addAll(resourceRoles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList()));
                }
            });
        }

        return authorities;
    }
}