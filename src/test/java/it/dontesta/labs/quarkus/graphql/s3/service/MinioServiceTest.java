/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.s3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.test.junit.QuarkusTest;
import it.dontesta.labs.quarkus.graphql.exception.MinioServiceException;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MinioServiceTest {

    @Inject
    MinioService minioService;

    @Test
    void uploadObjectWithValidFilePath() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        String filePath = "src/test/resources/test-file.txt";

        minioService.uploadObject(bucketName, objectName, filePath);

        InputStream object = minioService.getObject(bucketName, objectName);
        assertNotNull(object);
    }

    @Test
    void uploadObjectWithEmptyFileThrowsException() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        String filePath = "src/test/resources/empty-file.txt";

        assertThrows(MinioServiceException.class, () -> minioService.uploadObject(bucketName, objectName, filePath));
    }

    @Test
    void uploadObjectWithValidByteArray() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        byte[] fileContent = "This is a test file content".getBytes();

        minioService.uploadObject(bucketName, objectName, fileContent);

        InputStream object = minioService.getObject(bucketName, objectName);
        assertNotNull(object);
    }

    @Test
    void getObjectAsBase64ReturnsCorrectString() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        byte[] fileContent = "This is a test file content".getBytes();
        minioService.uploadObject(bucketName, objectName, fileContent);

        String base64Content = minioService.getObjectAsBase64(bucketName, objectName);
        assertEquals(Base64.getEncoder().encodeToString(fileContent), base64Content);
    }

    @Test
    @Order(1)
    void bucketExistsReturnsTrueForExistingBucket() {
        String bucketName = "test-bucket";
        minioService.makeBucket(bucketName);

        boolean exists = minioService.bucketExists(bucketName);
        assertEquals(true, exists);
    }

    @Test
    void bucketExistsReturnsFalseForNonExistingBucket() {
        String bucketName = "non-existing-bucket";

        boolean exists = minioService.bucketExists(bucketName);
        assertEquals(false, exists);
    }

    @Test
    void getObjectDetailsReturnsCorrectDetails() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        byte[] fileContent = "This is a test file content".getBytes();
        minioService.uploadObject(bucketName, objectName, fileContent);

        Map<String, Object> details = minioService.getObjectDetails(bucketName, objectName);
        assertEquals(bucketName, details.get("bucketName"));
        assertEquals(objectName, details.get("objectName"));
        assertEquals(Long.valueOf(fileContent.length), details.get("size"));
    }

    @Test
    void removeObjectRemovesObjectSuccessfully() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        byte[] fileContent = "This is a test file content".getBytes();
        minioService.uploadObject(bucketName, objectName, fileContent);

        minioService.removeObject(bucketName, objectName);

        assertThrows(
            MinioServiceException.class, () -> minioService.getObject(bucketName, objectName));
    }
}