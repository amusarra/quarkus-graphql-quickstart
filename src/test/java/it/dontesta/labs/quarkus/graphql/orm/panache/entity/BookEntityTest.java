/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.orm.panache.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class BookEntityTest {

    @Inject
    EntityManager em;

    @Test
    @Transactional
    void createBook_persistsBook() {
        Book book = new Book();
        book.title = "Test Book";
        book.isbn = "1234567890123";
        book.pages = 300;
        book.summary = "Test summary";
        book.publication = LocalDate.now();
        book.genre = "Test Genre";
        book.languages = List.of("ENG");
        book.formats = List.of("PDF");
        book.keywords = List.of("test", "book");

        Editor editor = new Editor();
        editor.name = "Test Editor";
        book.editor = editor;

        Author author = new Author();
        author.firstName = "Test Author";
        author.lastName = "Doe";
        author.sex = "M";
        author.birthDate = LocalDate.of(1980, 1, 1);
        book.authors = List.of(author);

        em.persist(book);
        em.flush();

        Book persistedBook = em.find(Book.class, book.id);
        assertNotNull(persistedBook);
        assertEquals("Test Book", persistedBook.title);
        assertEquals("1234567890123", persistedBook.isbn);
        assertEquals(300, persistedBook.pages);
        assertEquals("Test summary", persistedBook.summary);
        assertEquals(LocalDate.now(), persistedBook.publication);
        assertEquals("Test Genre", persistedBook.genre);
        assertEquals(1, persistedBook.languages.size());
        assertEquals("ENG", persistedBook.languages.getFirst());
        assertEquals(1, persistedBook.formats.size());
        assertEquals("PDF", persistedBook.formats.getFirst());
        assertEquals(2, persistedBook.keywords.size());
        assertEquals("test", persistedBook.keywords.get(0));
        assertEquals("book", persistedBook.keywords.get(1));
        assertNotNull(persistedBook.editor);
        assertEquals("Test Editor", persistedBook.editor.name);
        assertEquals(1, persistedBook.authors.size());
        assertEquals("Test Author", persistedBook.authors.getFirst().firstName);
    }
}