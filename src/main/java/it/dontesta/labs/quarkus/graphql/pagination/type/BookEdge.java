/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.pagination.type;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import org.eclipse.microprofile.graphql.Type;

/**
 * Represents an edge in a connection, containing a node and a cursor.
 */
@Type
public class BookEdge {

    /**
     * The node of type Book.
     */
    private final Book node;

    /**
     * The cursor for this edge.
     */
    private final String cursor;

    /**
     * Constructs a new BookEdge instance.
     *
     * @param node the Book node
     * @param cursor the cursor for this edge
     */
    private BookEdge(Book node, String cursor) {
        this.node = node;
        this.cursor = cursor;
    }

    /**
     * Creates a new BookEdge instance.
     *
     * @param node the Book node
     * @param cursor the cursor for this edge
     * @return a new BookEdge instance
     */
    public static BookEdge create(Book node, String cursor) {
        return new BookEdge(node, cursor);
    }

    /**
     * Returns the Book node.
     *
     * @return the Book node
     */
    public Book getNode() {
        return node;
    }

    /**
     * Returns the cursor for this edge.
     *
     * @return the cursor
     */
    public String getCursor() {
        return cursor;
    }
}