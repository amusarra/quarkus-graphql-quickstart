/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.orm.panache.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "book")
public class Book extends PanacheEntity {
    @Column(length = 60, nullable = false)
    public String title;

    @Column(name = "sub_title", length = 60, nullable = true)
    public String subTitle;

    @Column(length = 13, nullable = false)
    public String isbn;

    @Column(length = 4, nullable = false)
    public Integer pages;

    @Column(length = 255, nullable = false)
    public String summary;

    @Column(name = "publication_date", nullable = false)
    public LocalDate publication;

    @Column(length = 20, nullable = false)
    public String genre;

    @ElementCollection
    @CollectionTable(name = "book_languages")
    @Column(name = "language", length = 3, nullable = false)
    public List<String> languages;

    @ElementCollection
    @CollectionTable(name = "book_formats")
    @Column(name = "format", length = 10, nullable = false)
    @Pattern(regexp = "^(EPUB|PDF|MOBI|AUDIO)$", message = "The admitted values for the format attribute are: 'EPUB', 'PDF', 'MOBI' or 'AUDIO'")
    public List<String> formats;

    @ElementCollection
    @CollectionTable(name = "book_keywords")
    @Column(name = "keyword", length = 255, nullable = false)
    public List<String> keywords;

    @ManyToMany
    @JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    public List<Author> authors;

    @ManyToOne
    public Editor editor;
}