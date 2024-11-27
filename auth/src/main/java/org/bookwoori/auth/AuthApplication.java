package org.bookwoori.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(servers = {
	@Server(url = "http://localhost:8000", description = "Local Gateway Server"),
	@Server(url = "http://localhost:8001", description = "Local Development Server"),
	@Server(url = "https://api.bookwoori.p-e.kr", description = "Production Server")
})
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
