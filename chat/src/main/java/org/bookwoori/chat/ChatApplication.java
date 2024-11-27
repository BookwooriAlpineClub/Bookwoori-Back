package org.bookwoori.chat;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableDiscoveryClient
@EnableFeignClients
@OpenAPIDefinition(servers = {
    @Server(url = "http://localhost:8000", description = "Local Gateway Server"),
    @Server(url = "http://localhost:8081", description = "Local Development Server"),
    @Server(url = "https://api.bookwoori.p-e.kr", description = "Production Server")
})
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}
