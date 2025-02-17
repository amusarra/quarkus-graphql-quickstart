/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.orm.panache.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "author")
public class Author extends PanacheEntity {

    @Column(name = "first_name", length = 60, nullable = false)
    public String firstName;

    @Column(name = "last_name", length = 60, nullable = false)
    public String lastName;

    @Column(length = 1, nullable = false)
    @Pattern(regexp = "^[MF]$", message = "The admitted values for the sex attribute are: 'M' or 'F'")
    public String sex;

    @Column(name = "birth_date", nullable = false)
    public LocalDate birthDate;

    @ManyToMany(mappedBy = "authors")
    @JsonBackReference
    public List<Book> books;

    /**
     * Finds all authors.
     *
     * @return a list of all authors.
     */
    public static List<Author> findAllAuthors() {
        return listAll();
    }

    /**
     * Finds an author by their ID.
     *
     * @param id the ID of the author to find.
     * @return the author with the specified ID, or null if no such author exists.
     */
    public static Author findAuthorById(Long id) {
        return findById(id);
    }

    /**
     * Finds authors by a list of IDs.
     *
     * @param query the query to execute.
     * @param authorIds the list of author IDs to find.
     * @return a list of authors matching the specified IDs.
     */
    public static List<Author> listByAuthorList(String query, List<Long> authorIds) {
        return list(query, authorIds);
    }
}