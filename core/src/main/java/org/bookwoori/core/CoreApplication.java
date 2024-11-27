package org.bookwoori.core;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
@OpenAPIDefinition(
    servers = {
        @Server(url = "http://localhost:8000", description = "Local Gateway Server"),
        @Server(url = "http://localhost:8080", description = "Local Development Server"),
        @Server(url = "https://api.bookwoori.p-e.kr", description = "Production Server")
    }
)
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

}
