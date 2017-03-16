# graphql-jpa-spring-boot-starter
Spring Boot starter for GraphQL JPA; Expose JPA entities with [GraphQL](http://graphql.org/).

Builds on [GraphQL for JPA](https://github.com/jcrygier/graphql-jpa),
which in turn builds on [GraphQL Java](https://github.com/graphql-java/graphql-java)
to expose JPA Entities through a `/graphql` endpoint.

Many thanks to @jcrygier for the initial hard work; This is mostly just a wrapper.

## Usage 
1. Add the following dependency to your project `pom.xml` along with the repository. 
  ```pom.xml
	<dependencies>
		<!-- Expose JPA entities using GraphQL -->
		<dependency>
			<groupId>com.github.timtebeek</groupId>
			<artifactId>graphql-jpa-spring-boot-starter</artifactId>
			<version>0.0.1</version>
		</dependency>
	</dependencies>
  ...
	<repositories>
		<repository>
			<id>bintray-timtebeek-maven</id>
			<name>bintray</name>
			<url>http://dl.bintray.com/timtebeek/maven</url>
		</repository>
	</repositories>
```
2. Start your project and navigate to http://localhost:8080/graphiql.html.
