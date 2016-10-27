package com.github.timtebeek.graphql.jpa;
import java.util.Map;

import org.crygier.graphql.GraphQLExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ExecutionResult;
import lombok.Data;

@RestController
@ConditionalOnWebApplication
public class GraphQLController {
	@Autowired
	private GraphQLExecutor	graphQLExecutor;

	@Autowired
	private ObjectMapper	objectMapper;

	@PostMapping("${graphql.jpa.path:/graphql}")
	public ExecutionResult graphQl(@RequestBody final GraphQLInputQuery query) throws Exception {
		Map<String, Object> variables = query.getVariables() != null ? objectMapper.readValue(query.getVariables(), Map.class) : null;
		return graphQLExecutor.execute(query.getQuery(), variables);
	}
}

@Data
class GraphQLInputQuery {
	String	query;
	String	variables;
}