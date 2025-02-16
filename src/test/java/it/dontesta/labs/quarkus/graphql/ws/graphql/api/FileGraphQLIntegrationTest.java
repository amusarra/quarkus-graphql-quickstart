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
class FileGraphQLIntegrationTest {

    @Test
    @Order(3)
    void getFile_returnsFileDetails() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"{ getFile(objectName: \\\"test.txt\\\", bucketName: \\\"test-bucket\\\") { objectName bucketName url content contentType size eTag } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.getFile.objectName", equalTo("test.txt"))
                .body("data.getFile.bucketName", equalTo("test-bucket"))
                .body("data.getFile.url", containsString("test-bucket/test.txt"))
                .body("data.getFile.content", equalTo("dGVzdCBjb250ZW50"))
                .body("data.getFile.contentType", equalTo("application/octet-stream"))
                .body("data.getFile.size", equalTo(12))
                .body("data.getFile.eTag", nullValue());
    }

    @Test
    @Order(1)
    void getFile_throwsGraphQLExceptionOnFailure() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"{ getFile(objectName: \\\"nonexistent.txt\\\", bucketName: \\\"test-bucket\\\") { objectName bucketName url content contentType size eTag } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("errors[0].message", containsString("Failed to retrieve object details from MinIO"));
    }

    @Test
    @Order(2)
    void uploadFile_returnsUploadedFileDetails() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"mutation { uploadFile(objectName: \\\"test.txt\\\", bucketName: \\\"test-bucket\\\", content: \\\"dGVzdCBjb250ZW50\\\") { objectName bucketName url } }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.uploadFile.objectName", equalTo("test.txt"))
                .body("data.uploadFile.bucketName", equalTo("test-bucket"))
                .body("data.uploadFile.url", containsString("test-bucket/test.txt"));
    }

    @Test
    @Order(4)
    void deleteFile_returnsTrueOnSuccess() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"query\": \"mutation { deleteFile(objectName: \\\"test.txt\\\", bucketName: \\\"test-bucket\\\") }\"}")
                .when()
                .post("/api/graphql")
                .then()
                .statusCode(200)
                .body("data.deleteFile", equalTo(true));
    }
}