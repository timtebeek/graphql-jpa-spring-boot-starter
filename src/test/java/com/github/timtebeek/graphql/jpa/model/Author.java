package com.github.timtebeek.graphql.jpa.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Author {
	@Id
	Long id;

	String name;

	@OneToMany(mappedBy="author")
	Collection<Book> books;
}
