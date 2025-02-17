/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @GET
    public List<Author> list() {
        return Author.findAllAuthors();
    }

    @GET
    @Path("/{id}")
    public Author get(@PathParam("id") Long id) {
        Author author = Author.findAuthorById(id);
        if (author == null) {
            throw new NotFoundException();
        }
        return author;
    }

    @POST
    @Transactional
    public Response create(Author author) {
        author.persist();
        return Response.created(URI.create("/authors/" + author.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Author update(@PathParam("id") Long id, Author author) {
        Author existingAuthor = Author.findAuthorById(id);
        if (existingAuthor == null) {
            throw new NotFoundException();
        }
        existingAuthor.firstName = author.firstName;
        existingAuthor.lastName = author.lastName;
        existingAuthor.sex = author.sex;
        existingAuthor.birthDate = author.birthDate;

        //... update other fields
        existingAuthor.persist();
        return existingAuthor;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Author author = Author.findAuthorById(id);
        if (author == null) {
            throw new NotFoundException();
        }
        author.delete();
    }
}