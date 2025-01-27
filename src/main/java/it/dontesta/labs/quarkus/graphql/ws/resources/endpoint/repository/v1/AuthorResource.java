package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Author;
import jakarta.validation.constraints.NotNull;
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
        return Author.listAll();
    }

    @GET
    @Path("/{id}")
    public Author get(@PathParam("id") Long id) {
        return Author.findById(id);
    }

    @POST
    public Response create(Author author) {
        author.persist();
        return Response.created(URI.create("/authors/" + author.id)).build();
    }

    @PUT
    @Path("/{id}")
    public Author update(@PathParam("id") Long id, Author author) {
        Author existingAuthor = Author.findById(id);
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
    public void delete(@PathParam("id") Long id) {
        Author author = Author.findById(id);
        if (author == null) {
            throw new NotFoundException();
        }
        author.delete();
    }
}