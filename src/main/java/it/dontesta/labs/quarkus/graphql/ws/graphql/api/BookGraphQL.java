/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class BookGraphQL {

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
    public Book createBook(Book book) {
        book.persist();
        return book;
    }

    @Mutation
    @Description("Delete a book by id")
    @Transactional
    public Book addAuthorsToBook(@Name("bookId") Long bookId, List<Long> authorIds) {
        Book book = getBook(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        List<Author> authors = Author.list("id in ?1", authorIds);
        book.authors.addAll(authors);
        return book;
    }
}