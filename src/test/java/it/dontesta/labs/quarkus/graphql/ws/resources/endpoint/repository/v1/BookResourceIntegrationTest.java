/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import io.quarkus.test.junit.QuarkusTest;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class BookResourceIntegrationTest {

    @Test
    void getAll_returnsAllBooks() {
        given()
                .when().get("/api/books")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    @Transactional
    void create_createsNewBook() {
        Book book = new Book();
        book.title = "New Book";
        book.formats = List.of("EPUB");
        book.pages = 5406;
        book.isbn = "123345435";
        book.genre = "Fiction";
        book.summary = "This is a book";
        book.publication = LocalDate.now();
        book.editor = new Editor();
        book.editor.name = "Editor";

        given()
                .contentType("application/json")
                .body(book)
                .when().post("/api/books")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("title", is("New Book"))
                .body("editor.name", is("Editor"));
    }

    // Add more tests here
}