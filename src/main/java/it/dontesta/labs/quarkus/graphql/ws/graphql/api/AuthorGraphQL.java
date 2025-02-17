/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
@ApplicationScoped
public class AuthorGraphQL {

    @Query
    @Description("Get all authors")
    public List<Author> allAuthors() {
        return Author.findAllAuthors();
    }

    @Query
    @Description("Get an author by id")
    public Author getAuthor(@Name("authorId") Long id) {
        return Author.findAuthorById(id);
    }

    @Mutation
    @Description("Create a new author")
    @Transactional
    public Author createAuthor(Author author) {
        // The author is persisted automatically by Panache
        // because it is a Panache entity.
        // Extend this method to handle the detached entity as needed.
        author.persist();
        return author;
    }

    @Mutation
    @Description("Delete an author by id")
    @Transactional
    public Author updateAuthor(@Name("authorId") Long id, Author authorData)
            throws GraphQLException {
        Author author = getAuthor(id);
        if (author == null) {
            throw new GraphQLException("Author not found with Id %d".formatted(id));
        }
        author.firstName = authorData.firstName;
        author.lastName = authorData.lastName;
        author.sex = authorData.sex;
        author.birthDate = authorData.birthDate;
        author.persist();
        return author;
    }
}