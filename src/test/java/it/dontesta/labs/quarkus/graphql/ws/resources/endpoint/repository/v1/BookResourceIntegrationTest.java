/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import io.quarkus.test.junit.QuarkusTest;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
                .statusCode(201)
                .header("Location", is(notNullValue()));
    }

    @Test
    void get_returnsBookById() {
        Long bookId = 5L; // Assumes a book with ID 1 exists

        given()
                .when().get("/api/books/" + bookId)
                .then()
                .statusCode(200)
                .body("id", is(bookId.intValue()));
    }

    @Test
    @Transactional
    void update_updatesExistingBook() {
        Long bookId = 5L; // Assumes a book with ID 1 exists
        Book book = new Book();
        book.title = "Updated Book";
        book.formats = List.of("PDF");
        book.pages = 123;
        book.isbn = "987654321";
        book.genre = "Non-Fiction";
        book.summary = "This is an updated book";
        book.publication = LocalDate.now();
        book.editor = new Editor();
        book.editor.name = "Updated Editor";
        book.languages = List.of("EN");
        book.keywords = List.of("Updated", "Book");

        given()
                .contentType("application/json")
                .body(book)
                .when().put("/api/books/" + bookId)
                .then()
                .statusCode(200)
                .body("title", is("Updated Book"));
    }

    @Test
    @Transactional
    void delete_deletesExistingBook() {
        Long bookId = 1L; // Assumes a book with ID 1 exists

        given()
                .when().delete("/api/books/" + bookId)
                .then()
                .statusCode(204);
    }
}