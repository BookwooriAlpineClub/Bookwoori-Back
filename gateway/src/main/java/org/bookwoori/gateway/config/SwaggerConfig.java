package org.bookwoori.gateway.config;

import java.util.Arrays;
import java.util.HashSet;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
public class SwaggerConfig {

    @Primary
    @Bean
    public SwaggerUiConfigProperties swaggerUiConfig() {
        SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();
        properties.setUrls(new HashSet<>(Arrays.asList(
            new SwaggerUrl("Auth Service", "/v3/api-docs/auth", "Auth"),
            new SwaggerUrl("Core Service", "/v3/api-docs/core", "Core"),
            new SwaggerUrl("Chat Service", "/v3/api-docs/chat", "Chat"),
            new SwaggerUrl("Notification Service", "/v3/api-docs/notification", "Notification")
        )));
        return properties;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source =
            new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}

