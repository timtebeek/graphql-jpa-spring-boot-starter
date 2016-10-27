package com.github.timtebeek.graphql.jpa;

import javax.persistence.EntityManager;

import org.crygier.graphql.GraphQLExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@SuppressWarnings("static-method")
public class GraphqlJpaAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean(GraphQLExecutor.class)
	public GraphQLExecutor graphQLExecutor(final EntityManager entityManager) {
		return new GraphQLExecutor(entityManager);
	}
}
