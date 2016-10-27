package com.github.timtebeek.graphql.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphqlJpaTestApplication {
	public static void main(final String[] args) {
		SpringApplication.run(GraphqlJpaAutoConfiguration.class, args);
	}
}
