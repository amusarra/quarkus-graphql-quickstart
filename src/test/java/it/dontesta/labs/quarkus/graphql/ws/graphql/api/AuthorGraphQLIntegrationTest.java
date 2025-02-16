/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorGraphQLIntegrationTest {

    @Test
    @Order(1)
    void allAuthors_returnsAllAuthors() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"{ allAuthors { firstName lastName } }\"}")
                .when().post("/api/graphql")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void getAuthor_returnsAuthorById() {
        Long authorId = 5L; // Assumes an author with ID 5 exists

        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"query getAuthor { author(authorId: " + authorId + ") { id firstName lastName } }\"}")
                .when().post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.author.id", is(authorId.intValue()));
    }

    @Test
    @Order(3)
    void createAuthor_createsNewAuthor() {
        String query = "{\"query\": \"mutation { createAuthor(author: { firstName: \\\"John\\\", lastName: \\\"Doe\\\", sex: \\\"M\\\", birthDate: \\\"1980-01-01\\\" }) { id firstName lastName } }\"}";

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.createAuthor.id", notNullValue())
                .body("data.createAuthor.firstName", equalTo("John"))
                .body("data.createAuthor.lastName", equalTo("Doe"));
    }

    @Test
    @Order(4)
    void updateAuthor_updatesExistingAuthor() {
        Long authorId = 5L; // Assumes an author with ID 5 exists
        String query = "{\"query\": \"mutation { updateAuthor(authorId: " + authorId
                + ", authorData: { firstName: \\\"Jane\\\", lastName: \\\"Doe\\\", sex: \\\"F\\\", birthDate: \\\"1985-01-01\\\" }) { id firstName lastName } }\"}";

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.updateAuthor.id", is(authorId.intValue()))
                .body("data.updateAuthor.firstName", equalTo("Jane"))
                .body("data.updateAuthor.lastName", equalTo("Doe"));
    }

    @Test
    @Order(5)
    void updateAuthor_throwsGraphQLExceptionForNonExistentAuthor() {
        Long nonExistentAuthorId = 999L; // Assumes an author with this ID does not exist
        String query = "{\"query\": \"mutation { updateAuthor(authorId: " + nonExistentAuthorId
                + ", authorData: { firstName: \\\"Jane\\\", lastName: \\\"Doe\\\", sex: \\\"F\\\", birthDate: \\\"1985-01-01\\\" }) { id firstName lastName } }\"}";

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("errors[0].message", containsString("Author not found with Id " + nonExistentAuthorId));
    }
}