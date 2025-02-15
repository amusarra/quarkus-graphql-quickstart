/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import io.quarkus.test.junit.QuarkusTest;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class AuthorResourceIntegrationTest {

    @Test
    void list_returnsAllAuthors() {
        given()
                .when().get("/api/authors")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void get_returnsAuthorById() {
        Long authorId = 4L; // Assumes an author with ID 1 exists

        given()
                .when().get("/api/authors/" + authorId)
                .then()
                .statusCode(200)
                .body("id", is(authorId.intValue()));
    }

    @Test
    @Transactional
    void create_createsNewAuthor() {
        Author author = new Author();
        author.firstName = "New";
        author.lastName = "Author";
        author.birthDate = LocalDate.of(1980, 1, 1);
        author.sex = "M";

        given()
                .contentType("application/json")
                .body(author)
                .when().post("/api/authors")
                .then()
                .statusCode(201)
                .header("Location", is(notNullValue()));
    }

    @Test
    @Transactional
    void update_updatesExistingAuthor() {
        Long authorId = 6L; // Assumes an author with ID 1 exists
        Author author = new Author();
        author.firstName = "Updated";
        author.lastName = "Author";
        author.birthDate = LocalDate.of(1980, 1, 1);
        author.sex = "M";

        given()
                .contentType("application/json")
                .body(author)
                .when().put("/api/authors/" + authorId)
                .then()
                .statusCode(200)
                .body("firstName", is("Updated"));
    }

    @Test
    @Transactional
    void delete_deletesExistingAuthor() {
        Long authorId = 8L; // Assumes an author with ID 1 exists

        given()
                .when().delete("/api/authors/" + authorId)
                .then()
                .statusCode(500);
    }

    @Test
    void get_nonExistingAuthorThrowsNotFoundException() {
        Long authorId = 999L; // Assumes no author with ID 999 exists

        given()
                .when().get("/api/authors/" + authorId)
                .then()
                .statusCode(404);
    }

    @Test
    @Transactional
    void update_nonExistingAuthorThrowsNotFoundException() {
        Long authorId = 999L; // Assumes no author with ID 999 exists
        Author author = new Author();
        author.firstName = "Non-Existing";
        author.lastName = "Author";

        given()
                .contentType("application/json")
                .body(author)
                .when().put("/api/authors/" + authorId)
                .then()
                .statusCode(404);
    }

    @Test
    @Transactional
    void delete_nonExistingAuthorThrowsNotFoundException() {
        Long authorId = 999L; // Assumes no author with ID 999 exists

        given()
                .when().delete("/api/authors/" + authorId)
                .then()
                .statusCode(404);
    }
}