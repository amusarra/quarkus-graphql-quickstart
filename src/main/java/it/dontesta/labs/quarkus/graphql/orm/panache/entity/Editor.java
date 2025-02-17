/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.orm.panache.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity(name = "editor")
public class Editor extends PanacheEntity {

    @Column
    public String name;

    @OneToMany(mappedBy = "editor")
    @JsonBackReference
    public List<Book> books;

    /**
     * Finds all editors.
     *
     * @return a list of all editors.
     */
    public static PanacheQuery<Editor> findAllEditors() {
        return findAll();
    }

    /**
     * Finds an editor by their ID.
     *
     * @param id the ID of the editor to find.
     * @return the editor with the specified ID, or null if no such editor exists.
     */
    public static Editor findEditorById(Long id) {
        return findById(id);
    }

    /**
     * Finds all editors.
     *
     * @return a list of all editors.
     */
    public static List<Editor> listAllEditors() {
        return listAll();
    }
}