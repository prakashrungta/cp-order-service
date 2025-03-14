package com.restaurant.orders.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Configuration properties for the token converter.
 * This class holds the properties related to the token converter, such as resource ID and principal attribute.
 */
@Configuration
@ConfigurationProperties(prefix = "token.converter")
@Getter
@Setter
public class TokenConverterProperties {

    /**
     * The resource ID for the token converter.
     */
    private String resourceId;

    /**
     * The principal attribute for the token converter.
     */
    private String principalAttribute;

    /**
     * Gets the principal attribute as an Optional.
     *
     * @return an Optional containing the principal attribute if it is set, otherwise an empty Optional
     */
    public Optional<String> getPrincipalAttribute() {
        return Optional.ofNullable(principalAttribute);
    }
}