package com.github.timtebeek.graphql.jpa;

import java.util.Map;

import org.crygier.graphql.GraphQLExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionResult;
import lombok.Data;

@RestController
@ConditionalOnWebApplication
public class GraphQLController {
	@Autowired
	private GraphQLExecutor graphQLExecutor;

	@PostMapping("${graphql.jpa.path:/graphql}")
	public ExecutionResult graphql(@RequestBody final GraphQLInputQuery query) throws Exception {
		return graphQLExecutor.execute(query.getQuery(), query.getVariables());
	}

	@Data
	public static class GraphQLInputQuery {
		String query;
		Map<String, Object> variables;
	}
}