package com.github.timtebeek.graphql.jpa.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Book {
	@Id
	Long id;

	String title;

	@ManyToOne
	Author author;

	@Enumerated(EnumType.STRING)
	Genre genre;
}
