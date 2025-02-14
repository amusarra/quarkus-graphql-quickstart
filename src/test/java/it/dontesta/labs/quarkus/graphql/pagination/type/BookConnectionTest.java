/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.pagination.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.test.junit.QuarkusTest;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import org.junit.jupiter.api.Test;
import java.util.List;

@QuarkusTest
class BookConnectionTest {

    @Test
    void createBookConnectionWithValidParameters() {
        List<BookEdge> edges = List.of(BookEdge.create(new Book(), "MA=="));
        PageInfo pageInfo = PageInfo.create(true, "MA==");

        BookConnection bookConnection = BookConnection.create(edges, pageInfo);

        assertNotNull(bookConnection);
        assertEquals(edges, bookConnection.getEdges());
        assertEquals(pageInfo, bookConnection.getPageInfo());
    }

    @Test
    void createBookConnectionWithEmptyEdges() {
        List<BookEdge> edges = List.of();
        PageInfo pageInfo = PageInfo.create(false, "MA==");

        BookConnection bookConnection = BookConnection.create(edges, pageInfo);

        assertNotNull(bookConnection);
        assertEquals(edges, bookConnection.getEdges());
        assertEquals(pageInfo, bookConnection.getPageInfo());
    }

    @Test
    void createBookConnectionWithNullPageInfo() {
        List<BookEdge> edges = List.of(BookEdge.create(new Book(), "MA=="));

        BookConnection bookConnection = BookConnection.create(edges, null);

        assertNotNull(bookConnection);
        assertEquals(edges, bookConnection.getEdges());
        assertEquals(null, bookConnection.getPageInfo());
    }

    @Test
    void getNodeReturnsCorrectNode() {
        Book book = new Book();
        BookEdge edge = BookEdge.create(book, "MA==");

        assertEquals(book, edge.getNode());
    }

    @Test
    void getCursorReturnsCorrectCursor() {
        BookEdge edge = BookEdge.create(new Book(), "MA==");

        assertEquals("MA==", edge.getCursor());
    }

    @Test
    void getEndCursorReturnsCorrectEndCursor() {
        PageInfo pageInfo = PageInfo.create(true, "MA==");

        assertEquals("MA==", pageInfo.getEndCursor());
    }

    @Test
    void isHasNextPageReturnsCorrectValue() {
        PageInfo pageInfo = PageInfo.create(true, "MA==");

        assertEquals(true, pageInfo.isHasNextPage());
    }
}