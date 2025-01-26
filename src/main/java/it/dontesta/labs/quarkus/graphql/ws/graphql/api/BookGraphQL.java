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
    public List<Book> allBooks() {
        return Book.listAll();
    }

    @Query
    public Book getBook(Long id) {
        return Book.findById(id);
    }

    @Mutation
    @Transactional
    public Book createBook(Book book) {
        book.persist();
        return book;
    }

    @Mutation
    @Transactional
    public Book addAuthorsToBook(Long bookId, List<Long> authorIds) {
        Book book = getBook(bookId);
        if (book == null) {
            throw new NotFoundException("Book not found");
        }
        List<Author> authors = Author.list("id in ?1", authorIds);
        book.authors.addAll(authors);
        return book;
    }

    @Query
    public List<Author> allAuthors() {
        return Author.listAll();
    }
}