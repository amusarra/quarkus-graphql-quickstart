/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import it.dontesta.labs.quarkus.graphql.pagination.type.BookConnection;
import it.dontesta.labs.quarkus.graphql.pagination.type.BookEdge;
import it.dontesta.labs.quarkus.graphql.pagination.type.PageInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Base64;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
@ApplicationScoped
public class BookGraphQL {

    @Inject
    EntityManager entityManager;

    /**
     * Retrieves a paginated list of books.
     *
     * @param first the number of books to retrieve
     * @param after the cursor after which to start retrieving books
     * @return a BookConnection containing the list of books and pagination information
     * @throws GraphQLException if an error occurs during retrieval
     */
    @Query
    public BookConnection books(@Name("first") int first,
            @NotEmpty @NotNull @Name("after") String after)
            throws GraphQLException {

        int startIndex;

        // Decode the cursor to get the start index
        try {
            String decoded = new String(Base64.getDecoder().decode(after));
            startIndex = Integer.parseInt(decoded) + 1;
        } catch (IllegalArgumentException e) {
            throw new GraphQLException("Invalid cursor format", e);
        }

        // Query Panache to get the books
        PanacheQuery<Book> query = Book.findAll();
        List<Book> books = query.range(startIndex, startIndex + first - 1).list();

        // Create the edges response with the cursor
        List<BookEdge> edges = books.stream()
                .map(book -> {
                    String cursor = Base64.getEncoder().encodeToString(String.valueOf(book.id).getBytes());
                    return BookEdge.create(book, cursor);
                })
                .toList();

        // Check if there are more pages
        String endCursor = edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor();
        boolean hasNextPage = (startIndex + first) < query.count();

        return BookConnection.create(edges, PageInfo.create(hasNextPage, endCursor));
    }

    /**
     * Retrieves all books.
     *
     * @return a list of all books
     */
    @Query
    @Description("Get all books")
    public List<Book> allBooks() {
        return Book.listAll();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return the book with the specified ID
     */
    @Query
    @Description("Get a book by id")
    public Book getBook(@Name("bookId") Long id) {
        return Book.findById(id);
    }

    /**
     * Creates a new book and notifies subscribers.
     *
     * @param book the book to create
     * @return the created book
     * @throws GraphQLException if an error occurs during creation
     */
    @Mutation
    @Description("Create a new book")
    @Transactional
    public Book createBook(Book book) throws GraphQLException {

        // Handle the editor and authors
        handleEditor(book);
        handleAuthors(book);

        // Persist the book and flush to get the ID
        entityManager.persist(book);
        entityManager.flush();

        // Notify subscribers
        processor.onNext(book);

        return book;
    }

    /**
     * Adds authors to a book by its ID.
     *
     * @param bookId the ID of the book
     * @param authorIds the IDs of the authors to add
     * @return the updated book
     * @throws GraphQLException if the book or authors are not found
     */
    @Mutation
    @Description("Delete a book by id")
    @Transactional
    public Book addAuthorsToBook(@Name("bookId") Long bookId, List<Long> authorIds)
            throws GraphQLException {
        Book book = getBook(bookId);
        if (book == null) {
            throw new GraphQLException("Book not found with Id %d".formatted(bookId));
        }
        List<Author> authors = Author.list("id in ?1", authorIds);
        book.authors.addAll(authors);
        return book;
    }

    /**
     * Subscription method to notify subscribers when a new book is created.
     *
     * @return a Multi stream of Book objects representing the created books
     */
    @Subscription
    public Multi<Book> bookCreated() {
        return processor;
    }

    /**
     * Handles the editor of a book.
     *
     * @param book the book whose editor is to be handled
     * @throws GraphQLException if the editor is not found
     */
    private void handleEditor(Book book) throws GraphQLException {
        if (book.editor != null && book.editor.id != null) {
            Editor foundEditor = Editor.findById(book.editor.id);
            if (foundEditor == null) {
                throw new GraphQLException("Editor not found with Id %d".formatted(book.editor.id));
            }
            book.editor = foundEditor;
        }
    }

    /**
     * Handles the authors of a book.
     *
     * @param book the book whose authors are to be handled
     * @throws GraphQLException if any author is not found
     */
    private void handleAuthors(Book book) throws GraphQLException {
        if (book.authors != null) {
            List<Author> updatedAuthors = new ArrayList<>();
            for (Author author : book.authors) {
                if (author.id != null) {
                    Author foundAuthor = Author.findById(author.id);
                    if (foundAuthor == null) {
                        throw new GraphQLException("Author not found with Id %d".formatted(author.id));
                    }
                    updatedAuthors.add(foundAuthor);
                }
            }
            if (!updatedAuthors.isEmpty()) {
                book.authors = updatedAuthors;
            }
        }
    }

    // Broadcast processor to notify subscribers
    private final BroadcastProcessor<Book> processor = BroadcastProcessor.create();
}