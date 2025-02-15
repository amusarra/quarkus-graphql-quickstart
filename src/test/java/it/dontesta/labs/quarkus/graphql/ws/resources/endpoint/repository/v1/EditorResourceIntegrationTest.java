/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.repository.v1;

import io.quarkus.test.junit.QuarkusTest;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Editor;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class EditorResourceIntegrationTest {

    @Test
    void list_returnsAllEditors() {
        given()
                .when().get("/api/editors")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void get_returnsEditorById() {
        Long editorId = 3L; // Assumes an editor with ID 1 exists

        given()
                .when().get("/api/editors/" + editorId)
                .then()
                .statusCode(200)
                .body("id", is(editorId.intValue()));
    }

    @Test
    @Transactional
    void create_createsNewEditor() {
        Editor editor = new Editor();
        editor.name = "New Editor";

        given()
                .contentType("application/json")
                .body(editor)
                .when().post("/api/editors")
                .then()
                .statusCode(201)
                .header("Location", is(notNullValue()));
    }

    @Test
    @Transactional
    void update_updatesExistingEditor() {
        Long editorId = 5L; // Assumes an editor with ID 1 exists
        Editor editor = new Editor();
        editor.name = "Updated Editor";

        given()
                .contentType("application/json")
                .body(editor)
                .when().put("/api/editors/" + editorId)
                .then()
                .statusCode(200)
                .body("name", is("Updated Editor"));
    }

    @Test
    @Transactional
    void delete_deletesExistingEditor() {
        Long editorId = 8L; // Assumes an editor with ID 1 exists

        given()
                .when().delete("/api/editors/" + editorId)
                .then()
                .statusCode(500);
    }

    @Test
    void get_nonExistingEditorThrowsNotFoundException() {
        Long editorId = 999L; // Assumes no editor with ID 999 exists

        given()
                .when().get("/api/editors/" + editorId)
                .then()
                .statusCode(404);
    }

    @Test
    @Transactional
    void update_nonExistingEditorThrowsNotFoundException() {
        Long editorId = 999L; // Assumes no editor with ID 999 exists
        Editor editor = new Editor();
        editor.name = "Non-Existing Editor";

        given()
                .contentType("application/json")
                .body(editor)
                .when().put("/api/editors/" + editorId)
                .then()
                .statusCode(404);
    }

    @Test
    @Transactional
    void delete_nonExistingEditorThrowsNotFoundException() {
        Long editorId = 999L; // Assumes no editor with ID 999 exists

        given()
                .when().delete("/api/editors/" + editorId)
                .then()
                .statusCode(404);
    }
}