/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class BookGraphQL {

    @Inject
    EntityManager entityManager;

    @Query
    @Description("Get all books")
    public List<Book> allBooks() {
        return Book.listAll();
    }

    @Query
    @Description("Get a book by id")
    public Book getBook(@Name("bookId") Long id) {
        return Book.findById(id);
    }

    @Mutation
    @Description("Create a new book")
    @Transactional
    public Book createBook(Book book) throws GraphQLException {
        handleEditor(book);
        handleAuthors(book);

        entityManager.persist(book);
        entityManager.flush();

        return book;
    }

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

    private void handleEditor(Book book) throws GraphQLException {
        if (book.editor != null && book.editor.id != null) {
            Editor foundEditor = Editor.findById(book.editor.id);
            if (foundEditor == null) {
                throw new GraphQLException("Editor not found with Id %d".formatted(book.editor.id));
            }
            book.editor = foundEditor;
        }
    }

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
}