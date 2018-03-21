package com.indywiz.springorama.AmazonS3ResourcesExample;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class AmazonS3ResourcesExampleApplication {

	@Autowired
	S3UtilService service;

	public static void main(String[] args) {
		SpringApplication.run(AmazonS3ResourcesExampleApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			final List<String> listS3Files = service.listS3Files("bucketname");
			log.info("List of files in s3 bucket = {}", listS3Files);
		};
	}
}
