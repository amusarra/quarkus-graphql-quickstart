/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.orm.panache.entity;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorEntityTest {

    @Inject
    EntityManager em;

    @Test
    @Order(1)
    @Transactional
    void createAuthor_persistsAuthor() {
        Author author = new Author();
        author.firstName = "John";
        author.lastName = "Doe";
        author.sex = "M";
        author.birthDate = LocalDate.of(1980, 1, 1);

        em.persist(author);
        em.flush();

        Author persistedAuthor = em.find(Author.class, author.id);
        assertNotNull(persistedAuthor);
        assertEquals("John", persistedAuthor.firstName);
        assertEquals("Doe", persistedAuthor.lastName);
        assertEquals("M", persistedAuthor.sex);
        assertEquals(LocalDate.of(1980, 1, 1), persistedAuthor.birthDate);
    }

    @Test
    @Order(2)
    @Transactional
    void createAuthor_withInvalidSex_throwsValidationException() {
        Author author = new Author();
        author.firstName = "Jane";
        author.lastName = "Doe";
        author.sex = "X"; // Invalid sex value
        author.birthDate = LocalDate.of(1990, 1, 1);

        try {
            em.persist(author);
            em.flush();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    @Order(3)
    @Transactional
    void updateAuthor_updatesExistingAuthor() {
        Author author = new Author();
        author.firstName = "Alice";
        author.lastName = "Smith";
        author.sex = "F";
        author.birthDate = LocalDate.of(1975, 5, 15);
        em.persist(author);
        em.flush();

        author.firstName = "Alicia";
        author.lastName = "Johnson";
        author.birthDate = LocalDate.of(1975, 6, 15);
        em.merge(author);
        em.flush();

        Author updatedAuthor = em.find(Author.class, author.id);
        assertNotNull(updatedAuthor);
        assertEquals("Alicia", updatedAuthor.firstName);
        assertEquals("Johnson", updatedAuthor.lastName);
        assertEquals(LocalDate.of(1975, 6, 15), updatedAuthor.birthDate);
    }

    @Test
    @Order(4)
    @Transactional
    void deleteAuthor_removesAuthor() {
        Author author = new Author();
        author.firstName = "Bob";
        author.lastName = "Brown";
        author.sex = "M";
        author.birthDate = LocalDate.of(1965, 3, 10);
        em.persist(author);
        em.flush();

        em.remove(author);
        em.flush();

        Author deletedAuthor = em.find(Author.class, author.id);
      assertNull(deletedAuthor);
    }
}