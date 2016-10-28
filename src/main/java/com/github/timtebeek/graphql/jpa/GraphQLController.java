package com.github.timtebeek.graphql.jpa;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import graphql.ExecutionResult;
import lombok.Data;
import org.crygier.graphql.GraphQLExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnWebApplication
public class GraphQLController {
	@Autowired
	private GraphQLExecutor graphQLExecutor;

	@PostMapping("${graphql.jpa.path:/graphql}")
	public ExecutionResult graphql(@RequestBody @Valid final GraphQLInputQuery query) {
		return graphQLExecutor.execute(query.getQuery(), query.getVariables());
	}

	@Data
	public static class GraphQLInputQuery {
		@NotNull
		String				query;
		Map<String, Object>	variables;
	}
}
