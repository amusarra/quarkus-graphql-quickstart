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
    public List<BookEdge> edges;

    /**
     * Pagination information for the connection.
     */
    public PageInfo pageInfo;

    /**
     * Constructs a new BookConnection instance.
     *
     * @param edges a list of Book edges
     * @param pageInfo pagination information for the connection
     */
    public BookConnection(List<BookEdge> edges, PageInfo pageInfo) {
        this.edges = edges;
        this.pageInfo = pageInfo;
    }
}