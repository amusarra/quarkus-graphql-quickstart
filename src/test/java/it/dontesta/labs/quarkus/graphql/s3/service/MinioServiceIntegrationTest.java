/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.s3.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MinioServiceIntegrationTest {

    @Inject
    MinioService minioService;

    @Test
    void uploadAndGetObject() throws Exception {
        String bucketName = "integration-test-bucket";
        String objectName = "integration-test-object";
        String filePath = "/tmp/test.md";

        // Upload file
        minioService.uploadObject(bucketName, objectName, filePath);

        // Verify file upload
        InputStream object = minioService.getObject(bucketName, objectName);
        assertNotNull(object);

        // Clean up
        minioService.removeObject(bucketName, objectName);
    }

    @Test
    void bucketExistsIntegration() throws Exception {
        String bucketName = "integration-test-bucket";

        // Verify bucket existence
        boolean exists = minioService.bucketExists(bucketName);
        assertFalse(exists);
    }
}