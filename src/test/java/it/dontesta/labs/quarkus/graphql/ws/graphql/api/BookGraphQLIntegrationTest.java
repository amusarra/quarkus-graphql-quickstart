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
class BookGraphQLIntegrationTest {

    @Test
    @Order(1)
    void allBooks_returnsAllBooks() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"{ allBooks { id title } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.allBooks", notNullValue());
    }

    @Test
    @Order(2)
    void getBook_returnsBookById() {
        Long bookId = 5L; // Assumes a book with ID 1 exists
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"query getBook { book(bookId: " + bookId +
                        ") { id title } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.book.id", is(bookId.intValue()))
                .body("data.book.title", notNullValue());
    }

    @Test
    @Order(3)
    void createBook_createsNewBook() {
        String query = "{\"query\": \"mutation createBook { createBook(book: {title: \\\"Libro da author e editor esistenti\\\", subTitle: \\\"Creato con Quarkus + GraphQL\\\", isbn: \\\"7650986575646\\\", pages: 567, summary: \\\"Summary of the book\\\", publication: \\\"2025-01-28\\\", genre: \\\"fantasy\\\", languages: [\\\"IT\\\"], formats: [\\\"EPUD\\\", \\\"PDF\\\"], keywords: [\\\"key1\\\"], authors: [{id: 5}], editor: {id: 5}}) { id title }}\"}";

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.createBook.id", notNullValue())
                .body("data.createBook.title", equalTo("Libro da author e editor esistenti"));
    }

    @Test
    @Order(4)
    void addAuthorsToBook_addsAuthorsToBook() {
        Long bookId = 5L; // Assumes a book with ID 5 exists
        Long authorId = 5L; // Assumes an author with ID 5 exists

        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"mutation { addAuthorsToBook(bookId: " + bookId + ", authorIds: [" + authorId
                        + "]) { id title authors { id firstName lastName } } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.addAuthorsToBook.id", is(bookId.intValue()))
                .body("data.addAuthorsToBook.authors[0].id", is(7));
    }

    @Test
    @Order(5)
    void books_returnsPaginatedBooks() {
        int first = 2;
        String after = "MA=="; // Base64 encoded cursor, e.g., "0" encoded as "MA=="

        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"{ books(first: " + first + ", after: \\\"" + after
                        + "\\\") { edges { node { id title } cursor } pageInfo { hasNextPage endCursor } } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.books.edges", hasSize(first))
                .body("data.books.pageInfo.hasNextPage", is(true))
                .body("data.books.pageInfo.endCursor", notNullValue());
    }

    @Test
    @Order(6)
    void books_throwsGraphQLExceptionForInvalidCursor() {
        int first = 2;
        String invalidAfter = "invalid_cursor"; // Invalid cursor format

        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"{ books(first: " + first + ", after: \\\"" + invalidAfter
                        + "\\\") { edges { node { id title } cursor } pageInfo { hasNextPage endCursor } } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("errors[0].message", containsString("Invalid cursor format"));
    }

    @Test
    @Order(7)
    void addAuthorsToBook_throwsGraphQLExceptionForNonExistentBook() {
        Long nonExistentBookId = 999L; // Assumes a book with this ID does not exist
        Long authorId = 5L; // Assumes an author with ID 5 exists

        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"mutation { addAuthorsToBook(bookId: " + nonExistentBookId + ", authorIds: [" + authorId
                        + "]) { id title authors { id firstName lastName } } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("errors[0].message", containsString("Book not found with Id " + nonExistentBookId));
    }

    @Test
    @Order(8)
    void createBook_throwsGraphQLExceptionForNonExistentEditor() {
        String query = "{\"query\": \"mutation createBook { createBook(book: {title: \\\"Libro senza editor\\\", subTitle: \\\"Creato con Quarkus + GraphQL\\\", isbn: \\\"7650986575646\\\", pages: 567, summary: \\\"Summary of the book\\\", publication: \\\"2025-01-28\\\", genre: \\\"fantasy\\\", languages: [\\\"IT\\\"], formats: [\\\"EPUD\\\", \\\"PDF\\\"], keywords: [\\\"key1\\\"], authors: [{id: 5}], editor: {id: 999}}) { id title }}\"}";

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("errors[0].message", containsString("Editor not found with Id 999"));
    }

    @Test
    @Order(9)
    void createBook_throwsGraphQLExceptionForNonExistentAuthor() {
        String query = "{\"query\": \"mutation createBook { createBook(book: {title: \\\"Libro senza author\\\", subTitle: \\\"Creato con Quarkus + GraphQL\\\", isbn: \\\"7650986575646\\\", pages: 567, summary: \\\"Summary of the book\\\", publication: \\\"2025-01-28\\\", genre: \\\"fantasy\\\", languages: [\\\"IT\\\"], formats: [\\\"EPUD\\\", \\\"PDF\\\"], keywords: [\\\"key1\\\"], authors: [{id: 999}], editor: {id: 5}}) { id title }}\"}";

        given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("errors[0].message", containsString("Author not found with Id 999"));
    }
}