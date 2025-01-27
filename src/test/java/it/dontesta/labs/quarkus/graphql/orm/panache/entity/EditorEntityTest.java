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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EditorEntityTest {

    @Inject
    EntityManager em;

    @Test
    @Order(1)
    @Transactional
    void createEditor_persistsEditor() {
        Editor editor = new Editor();
        editor.name = "Test Editor";

        em.persist(editor);
        em.flush();

        Editor persistedEditor = em.find(Editor.class, editor.id);
        assertNotNull(persistedEditor);
        assertEquals("Test Editor", persistedEditor.name);
    }

    @Test
    @Order(2)
    @Transactional
    void createEditor_withBooks_persistsEditorAndBooks() {
        Editor editor = new Editor();
        editor.name = "Editor with Books";

        Book book1 = new Book();
        book1.title = "Book 1";
        book1.editor = editor;

        Book book2 = new Book();
        book2.title = "Book 2";
        book2.editor = editor;

        editor.books = List.of(book1, book2);

        em.persist(editor);
        em.flush();

        Editor persistedEditor = em.find(Editor.class, editor.id);
        assertNotNull(persistedEditor);
        assertEquals("Editor with Books", persistedEditor.name);
        assertEquals(2, persistedEditor.books.size());
        assertEquals("Book 1", persistedEditor.books.get(0).title);
        assertEquals("Book 2", persistedEditor.books.get(1).title);
    }

    @Test
    @Order(3)
    @Transactional
    void updateEditor_updatesExistingEditor() {
        Editor editor = new Editor();
        editor.name = "Old Editor";
        em.persist(editor);
        em.flush();

        editor.name = "Updated Editor";
        em.merge(editor);
        em.flush();

        Editor updatedEditor = em.find(Editor.class, editor.id);
        assertNotNull(updatedEditor);
        assertEquals("Updated Editor", updatedEditor.name);
    }

    @Test
    @Order(4)
    @Transactional
    void deleteEditor_removesEditor() {
        Editor editor = new Editor();
        editor.name = "Editor to Delete";
        em.persist(editor);
        em.flush();

        em.remove(editor);
        em.flush();

        Editor deletedEditor = em.find(Editor.class, editor.id);
        assertEquals(null, deletedEditor);
    }
}