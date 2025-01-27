package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLApi
public class EditorGraphQL {

    @Query
    public List<Editor> allEditors() {
        return Editor.listAll();
    }

    @Query
    public Editor getEditor(Long id) {
        return Editor.findById(id);
    }

    @Mutation
    @Transactional
    public Editor createEditor(Editor editor) {
        editor.persist();
        return editor;
    }

    @Mutation
    @Transactional
    public Editor updateEditor(Long id, Editor editorData) {
        Editor editor = getEditor(id);
        if (editor == null) {
            throw new NotFoundException("Editor not found");
        }
        editor.name = editorData.name;
        editor.persist();
        return editor;
    }
}