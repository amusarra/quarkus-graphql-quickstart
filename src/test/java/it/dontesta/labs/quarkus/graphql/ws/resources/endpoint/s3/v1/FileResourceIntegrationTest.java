/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.s3.v1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

@QuarkusTest
public class FileResourceIntegrationTest {

    @Test
    void uploadNonExistentFile() throws IOException {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        Path filePath = Files.createTempFile("empty-test-file", ".txt");

        given()
                .multiPart("bucketName", bucketName)
                .multiPart("objectName", objectName)
                .multiPart("objectFile", filePath.toFile(), MediaType.APPLICATION_OCTET_STREAM)
                .when()
                .post("/api/s3/files/upload")
                .then()
                .statusCode(500)
                .body("error", equalTo("Failed to upload object to MinIO"));
    }

    @Test
    void uploadFileSuccessfully() throws Exception {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        Path filePath = Files.createTempFile("test-file", ".txt");
        Files.writeString(filePath, "test content");

        given()
                .multiPart("bucketName", bucketName)
                .multiPart("objectName", objectName)
                .multiPart("objectFile", filePath.toFile(), MediaType.APPLICATION_OCTET_STREAM)
                .when()
                .post("/api/s3/files/upload")
                .then()
                .statusCode(200)
                .body("objectName", equalTo("test-object"));

        Files.deleteIfExists(filePath);
    }

    @Test
    void downloadFileSuccessfully() {
        String bucketName = "test-bucket";
        String objectName = "test-object";

        given()
                .pathParam("bucketName", bucketName)
                .pathParam("objectName", objectName)
                .when()
                .get("/api/s3/files/download/{bucketName}/{objectName}")
                .then()
                .statusCode(200)
                .header("Content-Disposition", "attachment; filename=\"" + objectName + "\"")
                .body(equalTo("test content"));
    }
}