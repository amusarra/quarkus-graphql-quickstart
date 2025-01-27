package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class AuthorGraphQL {

    @Query
    @Description("Get all authors")
    public List<Author> allAuthors() {
        return Author.listAll();
    }

    @Query
    @Description("Get an author by id")
    public Author getAuthor(@Name("authorId") Long id) {
        return Author.findById(id);
    }

    @Mutation
    @Description("Create a new author")
    @Transactional
    public Author createAuthor(Author author) {
        author.persist();
        return author;
    }

    @Mutation
    @Description("Delete an author by id")
    @Transactional
    public Author updateAuthor(@Name("authorId") Long id, Author authorData) {
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