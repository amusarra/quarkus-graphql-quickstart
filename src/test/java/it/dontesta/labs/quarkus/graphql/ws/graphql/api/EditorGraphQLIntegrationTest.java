/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EditorGraphQLIntegrationTest {

    @Test
    @Order(1)
    void allEditors_returnsAllEditors() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"{ allEditors { id name } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.allEditors", notNullValue());
    }

    @Test
    @Order(2)
    void getEditor_returnsEditorById() {
        Long editorId = 5L; // Assumes an editor with ID 5 exists
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"query getEditor { editor(editorId: " + editorId + ") { id name } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.editor.id", equalTo(editorId.intValue()))
                .body("data.editor.name", notNullValue());
    }

    @Test
    @Order(3)
    void createEditor_createsNewEditor() {
        String editorName = "New Editor";

        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"mutation { createEditor(editor: { name: \\\"" + editorName + "\\\" }) { id name } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.createEditor.id", notNullValue())
                .body("data.createEditor.name", equalTo(editorName));
    }

    @Test
    @Order(4)
    void updateEditor_updatesExistingEditor() {
        Long editorId = 5L; // Assumes an editor with ID 1 exists
        String updatedName = "Updated Editor";

        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"mutation updateEditor { updateEditor(editorId: " + editorId + " , editorData: { name: \\\""
                        + updatedName + "\\\" }) { id name } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.updateEditor.id", is(editorId.intValue()))
                .body("data.updateEditor.name", equalTo(updatedName));
    }
}