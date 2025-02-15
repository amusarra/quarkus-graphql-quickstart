/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class AuthorGraphQLIntegrationTest {

    @Test
    void allAuthors_returnsAllAuthors() {
        given()
                .contentType("application/json")
                .body("{\"query\": \"{ allAuthors { firstName lastName } }\"}")
                .when().post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.allAuthors", notNullValue());
    }

    @Test
    void getAuthor_returnsAuthorById() {
        Long authorId = 5L; // Assumes an author with ID 5 exists

        given()
                .contentType("application/json")
                .body("{\"query\": \"query getAuthor { author(authorId: " + authorId + ") { id firstName lastName } }\"}")
                .when().post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.author.id", is(authorId.intValue()));

    }
}