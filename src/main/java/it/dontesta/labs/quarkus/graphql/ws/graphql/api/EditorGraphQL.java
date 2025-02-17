/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
@ApplicationScoped
public class EditorGraphQL {

    @Query
    @Description("Get all editors")
    public List<Editor> allEditors() {
        return Editor.listAllEditors();
    }

    @Query
    @Description("Get an editor by id")
    public Editor getEditor(@Name("editorId") Long id) {
        return Editor.findEditorById(id);
    }

    @Mutation
    @Description("Create a new editor")
    @Transactional
    public Editor createEditor(Editor editor) {
        // The editor is persisted automatically by Panache
        // because it is a Panache entity.
        // Extend this method to handle the detached entity as needed.
        editor.persist();
        return editor;
    }

    @Mutation
    @Description("Delete an editor by id")
    @Transactional
    public Editor updateEditor(@Name("editorId") Long id, Editor editorData) {
        Editor editor = getEditor(id);
        if (editor == null) {
            throw new NotFoundException("Editor not found");
        }
        editor.name = editorData.name;
        editor.persist();
        return editor;
    }
}