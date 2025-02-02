/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.pagination.type;

import org.eclipse.microprofile.graphql.Type;

/**
 * Represents pagination information for a connection.
 */
@Type
public class PageInfo {

    /**
     * Indicates if there is a next page.
     */
    private final boolean hasNextPage;

    /**
     * The end cursor for the current page.
     */
    private final String endCursor;

    /**
     * Constructs a new PageInfo instance.
     *
     * @param hasNextPage indicates if there is a next page
     * @param endCursor the end cursor for the current page
     */
    private PageInfo(boolean hasNextPage, String endCursor) {
        this.hasNextPage = hasNextPage;
        this.endCursor = endCursor;
    }

    /**
     * Creates a new PageInfo instance.
     *
     * @param hasNextPage indicates if there is a next page
     * @param endCursor the end cursor for the current page
     * @return a new PageInfo instance
     */
    public static PageInfo create(boolean hasNextPage, String endCursor) {
        return new PageInfo(hasNextPage, endCursor);
    }

    /**
     * Returns if there is a next page.
     *
     * @return true if there is a next page, false otherwise
     */
    public boolean isHasNextPage() {
        return hasNextPage;
    }

    /**
     * Returns the end cursor for the current page.
     *
     * @return the end cursor
     */
    public String getEndCursor() {
        return endCursor;
    }
}