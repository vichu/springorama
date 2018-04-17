package com.indywiz.springorama.springintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

@SpringBootApplication
@EnableIntegrationGraphController(allowedOrigins = "http://localhost:8082")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
