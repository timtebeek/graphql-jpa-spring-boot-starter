package com.github.timtebeek.graphql.jpa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
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

	private void ok(final Query query) throws Exception, JsonProcessingException {
		perform(query).andExpect(status().isOk());
	}

	private ResultActions perform(final Query query) throws Exception, JsonProcessingException {
		return mockmvc.perform(post("/graphql").content(mapper.writeValueAsString(query)).contentType(MediaType.APPLICATION_JSON));
	}

	// Serialize a Query object
	@Test
	public void testGraphqlQuery() throws Exception {
		ok(new Query("{Book(title: \"title\"){title genre}}"));
	}

	@Test
	public void testGraphqlQueryNull() throws Exception {
		perform(new Query(null)).andExpect(status().isBadRequest());
	}

	@Test
	public void testGraphqlArguments() throws Exception {
		Query query = new Query("query BookQuery($title: String!){Book(title: $title){title genre}}");
		query.put("title", "value");
		ok(query);
	}

	// Json directly
	@Test
	public void testGraphqlArgumentsJson() throws Exception {
		String json = "{\"query\": \"{Book(title: \\\"title\\\"){title genre}\", \"arguments\": {\"title\": \"title\"}}";
		mockmvc.perform(post("/graphql").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGraphqlArgumentsEmptyString() throws Exception {
		String json = "{\"query\": \"{Book(title: \\\"title\\\"){title genre}\", \"arguments\": \"\"}";
		mockmvc.perform(post("/graphql").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGraphqlArgumentsNull() throws Exception {
		String json = "{\"query\": \"{Book(title: \\\"title\\\"){title genre}\", \"arguments\": null}";
		mockmvc.perform(post("/graphql").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}

@Data
class Query {
	final String		query;
	Map<String, Object>	arguments;

	void put(final String key, final Object value) {
		if (arguments == null)
			arguments = new HashMap<>();
		arguments.put(key, value);
	}
}
