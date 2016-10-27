package com.github.timtebeek.graphql.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import graphql.ExecutionResult;
import org.crygier.graphql.GraphQLExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.timtebeek.graphql.jpa.model.Book;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphQLExecutorTest {
	private static final String	WAR_AND_PEACE	= "War and Peace";

	@Autowired
	EntityManager				em;
	@Autowired
	GraphQLExecutor				graphqlExecutor;

	@Test
	public void getBook() {
		Book book = em.find(Book.class, Long.valueOf(2));
		Assert.assertNotNull(book);
		Assert.assertEquals(WAR_AND_PEACE, book.getTitle());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void query() {
		ExecutionResult result = graphqlExecutor.execute("{Book(title: \"" + WAR_AND_PEACE + "\"){title genre}}");
		Assert.assertTrue(result.getErrors().toString(), result.getErrors().isEmpty());
		Assert.assertEquals(WAR_AND_PEACE, ((Map<String, List<Map<String, Object>>>) result.getData()).get("Book").get(0).get("title"));
		Assert.assertEquals("{Book=[{title=War and Peace, genre=NOVEL}]}", result.getData().toString());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void queryArguments() {
		Map<String, Object> args = new HashMap<>();
		args.put("title", WAR_AND_PEACE);
		String query = "query BookQuery($title: String!){Book(title: $title){title genre}}";
		ExecutionResult result = graphqlExecutor.execute(query, args);
		Assert.assertTrue(result.getErrors().toString(), result.getErrors().isEmpty());
		Assert.assertEquals(WAR_AND_PEACE, ((Map<String, List<Map<String, Object>>>) result.getData()).get("Book").get(0).get("title"));
		Assert.assertEquals("{Book=[{title=War and Peace, genre=NOVEL}]}", result.getData().toString());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void queryArgumentsLongId() {
		Map<String, Object> args = new HashMap<>();
		args.put("bookid", Long.valueOf(2));
		String query = "query BookQuery($bookid: Long!){Book(id: $bookid){title genre}}";
		ExecutionResult result = graphqlExecutor.execute(query, args);
		Assert.assertTrue(result.getErrors().toString(), result.getErrors().isEmpty());
		Assert.assertEquals(WAR_AND_PEACE, ((Map<String, List<Map<String, Object>>>) result.getData()).get("Book").get(0).get("title"));
		Assert.assertEquals("{Book=[{title=War and Peace, genre=NOVEL}]}", result.getData().toString());
	}
}
