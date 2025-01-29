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
    public Book node;

    /**
     * The cursor for this edge.
     */
    public String cursor;

    /**
     * Constructs a new BookEdge instance.
     *
     * @param node the Book node
     * @param cursor the cursor for this edge
     */
    public BookEdge(Book node, String cursor) {
        this.node = node;
        this.cursor = cursor;
    }
}