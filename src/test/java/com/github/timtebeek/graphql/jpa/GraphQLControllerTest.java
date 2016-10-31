package com.github.timtebeek.graphql.jpa;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.timtebeek.graphql.jpa.GraphQLController.GraphQLInputQuery;
import org.crygier.graphql.GraphQLExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GraphQLController.class)
public class GraphQLControllerTest {
	@Autowired
	MockMvc			mockmvc;
	@MockBean
	GraphQLExecutor	executor;
	@Autowired
	ObjectMapper	mapper;

	private void ok(final GraphQLInputQuery query) throws Exception, JsonProcessingException {
		perform(mapper.writeValueAsString(query)).andExpect(status().isOk());
	}

	private void ok(final String json) throws Exception, JsonProcessingException {
		perform(json).andExpect(status().isOk());
	}

	private ResultActions perform(final String json) throws Exception {
		return mockmvc.perform(post("/graphql").content(json).contentType(MediaType.APPLICATION_JSON));
	}

	// Serialize a Query object
	@Test
	public void testGraphqlQuery() throws Exception {
		ok(new GraphQLInputQuery("{Book(title: \"title\"){title genre}}"));
		verify(executor).execute("{Book(title: \"title\"){title genre}}", null);
	}

	@Test
	public void testGraphqlQueryNull() throws Exception {
		perform(mapper.writeValueAsString(new GraphQLInputQuery(null))).andExpect(status().isBadRequest());
	}

	@Test
	public void testGraphqlArguments() throws Exception {
		GraphQLInputQuery query = new GraphQLInputQuery("query BookQuery($title: String!){Book(title: $title){title genre}}");
		Map<String, Object> variables = new HashMap<>();
		variables.put("title", "value");
		query.setVariables(variables);
		ok(query);
		verify(executor).execute(query.getQuery(), variables);
	}

	// Json directly
	@Test
	public void testGraphqlArgumentsJson() throws Exception {
		String json = "{\"query\": \"{Book(title: \\\"title\\\"){title genre}\", \"arguments\": {\"title\": \"title\"}}";
		ok(json);
		verify(executor).execute("{Book(title: \"title\"){title genre}", null);
	}

	@Test
	public void testGraphqlArgumentsEmptyString() throws Exception {
		String json = "{\"query\": \"{Book(title: \\\"title\\\"){title genre}\", \"arguments\": \"\"}";
		ok(json);
		verify(executor).execute("{Book(title: \"title\"){title genre}", null);
	}

	@Test
	public void testGraphqlArgumentsNull() throws Exception {
		String json = "{\"query\": \"{Book(title: \\\"title\\\"){title genre}\", \"arguments\": null}";
		ok(json);
		verify(executor).execute("{Book(title: \"title\"){title genre}", null);
	}

	// Form submitted data
	@Test
	public void testGraphqlArgumentsParams() throws Exception {
		String query = "{Book(title: \"title\"){title genre}}";
		mockmvc.perform(post("/graphql").param("query", query).contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk());
		verify(executor).execute(query, null);
	}

	@Test
	public void testGraphqlArgumentsParamsVariables() throws Exception {
		String query = "query BookQuery($title: String!){Book(title: $title){title genre}}";
		Map<String, Object> args = new HashMap<>();
		args.put("title", "value");
		String argsStr = mapper.writeValueAsString(args);
		mockmvc.perform(post("/graphql").param("query", query).param("variables", argsStr).contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk());
		verify(executor).execute(query, args);
	}

	@Test
	public void testGraphqlArgumentsParamsVariablesEmpty() throws Exception {
		String query = "{Book(title: \"title\"){title genre}}";
		mockmvc.perform(post("/graphql").param("query", query).param("variables", "").contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isOk());
		verify(executor).execute(query, null);
	}
}
