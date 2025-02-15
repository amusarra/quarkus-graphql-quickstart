/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.s3.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MinioServiceIntegrationTest {

    @Inject
    MinioService minioService;

    @Test
    void uploadAndGetObject() {
        String bucketName = "integration-test-bucket";
        String objectName = "integration-test-object";

        try {
            // Crea un file temporaneo
            File tempFile = File.createTempFile("tempFile", ".txt");

            // Scrivi del testo nel file temporaneo
          try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("This is a test file for MinIO integration test");
          }

          // Upload file
            minioService.uploadObject(bucketName, objectName, tempFile.getAbsolutePath());

            // Verify file upload
            InputStream object = minioService.getObject(bucketName, objectName);
            assertNotNull(object);

            // Clean up
            minioService.removeObject(bucketName, objectName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void bucketExistsIntegration() {
        String bucketName = "integration-test-bucket";

        // Verify bucket existence
        boolean exists = minioService.bucketExists(bucketName);
        assertFalse(exists);
    }
}