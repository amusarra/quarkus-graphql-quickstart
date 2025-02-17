/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/editors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EditorResource {

    @GET
    public List<Editor> list() {
        return Editor.listAllEditors();
    }

    @GET
    @Path("/{id}")
    public Editor get(@PathParam("id") Long id) {
        Editor editor = Editor.findEditorById(id);
        if (editor == null) {
            throw new NotFoundException();
        }
        return editor;
    }

    @POST
    @Transactional
    public Response create(Editor editor) {
        editor.persist();
        return Response.created(URI.create("/editors/" + editor.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Editor update(@PathParam("id") Long id, Editor editor) {
        Editor existingEditor = Editor.findEditorById(id);
        if (existingEditor == null) {
            throw new NotFoundException();
        }
        existingEditor.name = editor.name;
        existingEditor.persist();
        return existingEditor;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Editor editor = Editor.findEditorById(id);
        if (editor == null) {
            throw new NotFoundException();
        }
        editor.delete();
    }
}