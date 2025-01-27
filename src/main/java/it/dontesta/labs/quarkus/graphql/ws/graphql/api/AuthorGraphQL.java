package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class AuthorGraphQL {

    @Query
    public List<Author> allAuthors() {
        return Author.listAll();
    }

    @Query
    public Author getAuthor(Long id) {
        return Author.findById(id);
    }

    @Mutation
    @Transactional
    public Author createAuthor(Author author) {
        author.persist();
        return author;
    }

    @Mutation
    @Transactional
    public Author updateAuthor(Long id, Author authorData) {
        Author author = getAuthor(id);
        if (author == null) {
            throw new NotFoundException("Author not found");
        }
        author.firstName = authorData.firstName;
        author.lastName = authorData.lastName;
        author.sex = authorData.sex;
        author.birthDate = authorData.birthDate;
        author.persist();
        return author;
    }
}