/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.pagination.type;

import org.eclipse.microprofile.graphql.Type;
import java.util.List;

/**
 * Represents a connection to a list of Book edges with pagination information.
 */
@Type
public class BookConnection {

    /**
     * A list of Book edges.
     */
    private final List<BookEdge> edges;

    /**
     * Pagination information for the connection.
     */
    private final PageInfo pageInfo;

    /**
     * Constructs a new BookConnection instance.
     *
     * @param edges a list of Book edges
     * @param pageInfo pagination information for the connection
     */
    private BookConnection(List<BookEdge> edges, PageInfo pageInfo) {
        this.edges = edges;
        this.pageInfo = pageInfo;
    }

    /**
     * Creates a new BookConnection instance.
     *
     * @param edges a list of Book edges
     * @param pageInfo pagination information for the connection
     * @return a new BookConnection instance
     */
    public static BookConnection create(List<BookEdge> edges, PageInfo pageInfo) {
        return new BookConnection(edges, pageInfo);
    }

    /**
     * Returns the list of Book edges.
     *
     * @return the list of Book edges
     */
    public List<BookEdge> getEdges() {
        return edges;
    }

    /**
     * Returns the pagination information for the connection.
     *
     * @return the pagination information
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }
}