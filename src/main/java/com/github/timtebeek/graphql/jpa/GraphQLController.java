package com.github.timtebeek.graphql.jpa;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.crygier.graphql.GraphQLExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@ConditionalOnWebApplication
public class GraphQLController {
	private GraphQLExecutor	graphQLExecutor;
	private ObjectMapper	mapper;

	@PostMapping(value = "${graphql.jpa.path:/graphql}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ExecutionResult postJson(@RequestBody @Valid final GraphQLInputQuery query) {
		return graphQLExecutor.execute(query.getQuery(), query.getVariables());
	}

	@PostMapping(value = "${graphql.jpa.path:/graphql}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@SuppressWarnings("unchecked")
	public ExecutionResult postForm( //
			@RequestParam final String query, //
			@RequestParam(required = false) final String variables) throws IOException {
		Map<String, Object> variablesMap = null;
		if (variables != null && !variables.isEmpty())
			variablesMap = mapper.readValue(variables, Map.class);
		return graphQLExecutor.execute(query, variablesMap);
	}

	@Data
	public static class GraphQLInputQuery {
		@NotNull
		final String		query;
		Map<String, Object>	variables;
	}
}
