/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @GET
    public List<Book> list() {
        return Book.findAllBooksList();
    }

    @GET
    @Path("/{id}")
    public Book get(@PathParam("id") Long id) {
        Book book = Book.findBookById(id);
        if (book == null) {
            throw new NotFoundException();
        }
        return book;
    }

    @POST
    @Transactional
    public Response create(Book book) {
        // The book is persisted automatically by Panache
        // because it is a Panache entity.
        // Extend this method to handle the detached entity as needed.
        book.persist();
        return Response.created(URI.create("/books/" + book.id)).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Book update(@PathParam("id") Long id, Book book) {
        // The book is persisted automatically by Panache
        // because it is a Panache entity.
        // Extend this method to handle the detached entity as needed.

        Book entity = Book.findBookById(id);
        if (entity == null) {
            throw new NotFoundException();
        }

        // Update the entity with the new values
        // Extend this method to handle the updated entity as needed.
        entity.title = book.title;
        entity.editor = book.editor;
        entity.authors = book.authors;
        entity.languages = book.languages;
        entity.formats = book.formats;
        entity.keywords = book.keywords;
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Book entity = Book.findBookById(id);
        if (entity != null) {
            entity.delete();
        }
    }

    @PUT
    @Path("{id}/authors")
    @Transactional
    public Book addAuthors(@PathParam("id") Long id, List<Long> authorIds) {
        Book book = Book.findBookById(id);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        List<Author> authors = Author.listByAuthorList("id in ?1", authorIds);
        book.authors.addAll(authors);
        return book;
    }
}