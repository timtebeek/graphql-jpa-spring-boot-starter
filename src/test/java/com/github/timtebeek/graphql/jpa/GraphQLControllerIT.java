package com.github.timtebeek.graphql.jpa;

import java.util.List;
import java.util.Map;

import com.github.timtebeek.graphql.jpa.GraphQLController.GraphQLInputQuery;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import lombok.Value;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GraphQLControllerIT {
	private static final String	WAR_AND_PEACE	= "War and Peace";

	@Autowired
	TestRestTemplate			rest;

	@Test
	public void testGraphql() {
		GraphQLInputQuery query = new GraphQLInputQuery("{Book(title: \"" + WAR_AND_PEACE + "\"){title genre}}");

		ResponseEntity<Result> entity = rest.postForEntity("/graphql", new HttpEntity<>(query), Result.class);
		Assert.assertEquals(entity.toString(), HttpStatus.OK, entity.getStatusCode());

		Result result = entity.getBody();
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getErrors().toString(), result.getErrors().isEmpty());
		Assert.assertEquals(WAR_AND_PEACE, result.getData().get("Book").get(0).get("title"));
		Assert.assertEquals("{Book=[{title=War and Peace, genre=NOVEL}]}", result.getData().toString());
	}

	@Test
	public void testGraphqlArguments() {
		GraphQLInputQuery query = new GraphQLInputQuery("query BookQuery($title: String!){Book(title: $title){title genre}}");
		query.setVariables("{\"title\":\"" + WAR_AND_PEACE + "\"}");

		ResponseEntity<Result> entity = rest.postForEntity("/graphql", new HttpEntity<>(query), Result.class);
		Assert.assertEquals(entity.toString(), HttpStatus.OK, entity.getStatusCode());

		Result result = entity.getBody();
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getErrors().toString(), result.getErrors().isEmpty());
		Assert.assertEquals(WAR_AND_PEACE, result.getData().get("Book").get(0).get("title"));
		Assert.assertEquals("{Book=[{title=War and Peace, genre=NOVEL}]}", result.getData().toString());
	}
}

@Value
class Result implements ExecutionResult {
	Map<String, List<Map<String, Object>>>	data;
	List<GraphQLError>						errors;
}
