package it.dontesta.labs.quarkus.graphql.pagination.type;

import org.eclipse.microprofile.graphql.Type;

/**
 * Represents pagination information for GraphQL queries.
 */
@Type
public class PageInfo {

    /**
     * Indicates if there is a next page available.
     */
    public boolean hasNextPage;

    /**
     * The cursor to the end of the current page.
     */
    public String endCursor;

    /**
     * Constructs a new PageInfo instance.
     *
     * @param hasNextPage indicates if there is a next page available
     * @param endCursor the cursor to the end of the current page
     */
    public PageInfo(boolean hasNextPage, String endCursor) {
        this.hasNextPage = hasNextPage;
        this.endCursor = endCursor;
    }
}