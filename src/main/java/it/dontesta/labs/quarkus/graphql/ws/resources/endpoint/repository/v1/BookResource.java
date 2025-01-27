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
        return Book.listAll();
    }

    @GET
    @Path("/{id}")
    public Book get(@PathParam("id") Long id) {
        return Book.findById(id);
    }

    @POST
    @Transactional
    public Response create(Book book) {
        book.persist();
        return Response.created(URI.create("/books/" + book.id)).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Book update(@PathParam("id") Long id, Book book) {
        Book entity = Book.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.title = book.title;
        entity.editor = book.editor;
        entity.authors = book.authors;
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Book entity = Book.findById(id);
        if (entity != null) {
            entity.delete();
        }
    }

    @PUT
    @Path("{id}/authors")
    @Transactional
    public Book addAuthors(@PathParam("id") Long id, List<Long> authorIds) {
        Book book = Book.findById(id);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }

        List<Author> authors = Author.list("id in ?1", authorIds);
        book.authors.addAll(authors);
        return book;
    }
}