package org.bookwoori.notification;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@OpenAPIDefinition(servers = {
	@Server(url = "http://localhost:8000", description = "Local Gateway Server"),
	@Server(url = "http://localhost:8082", description = "Local Development Server"),
	@Server(url = "https://api.bookwoori.p-e.kr", description = "Production Server")
})
public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

}
