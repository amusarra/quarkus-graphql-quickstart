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
class MinioServiceIntegrationTest {

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
    @Order(2)
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

    @Test
    @Order(1)
    void removeObjectThrowsExceptionWithCorrectMessage() {
        String bucketName = "test-bucket";
        String objectName = "non-existing-object";

        MinioServiceException exception = assertThrows(
                MinioServiceException.class,
                () -> minioService.removeObject(bucketName, objectName));

        assertEquals("Failed to remove object from MinIO", exception.getMessage());
    }

    @Test
    void getObjectDetailsThrowsExceptionWithCorrectMessage() {
        String bucketName = "test-bucket";
        String objectName = "non-existing-object";

        MinioServiceException exception = assertThrows(
                MinioServiceException.class,
                () -> minioService.getObjectDetails(bucketName, objectName));

        assertEquals("Failed to retrieve object details from MinIO", exception.getMessage());
    }

    @Test
    void getObjectAsBase64ThrowsExceptionWithCorrectMessage() {
        String bucketName = "test-bucket";
        String objectName = "non-existing-object";

        MinioServiceException exception = assertThrows(
                MinioServiceException.class,
                () -> minioService.getObjectAsBase64(bucketName, objectName));

        assertEquals("Failed to retrieve object from MinIO", exception.getMessage());
    }

    @Test
    void uploadObjectThrowsMinioServiceException() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        String invalidFilePath = "src/test/resources/non-existent-file.txt"; // Non-existent file path

        MinioServiceException exception = assertThrows(
                MinioServiceException.class,
                () -> minioService.uploadObject(bucketName, objectName, invalidFilePath));

        assertEquals("Failed to upload object to MinIO", exception.getMessage());
    }

    @Test
    void uploadObjectCreatesBucketIfNotExists() {
        String bucketName = "new-bucket";
        String objectName = "test-object";
        String filePath = "src/test/resources/test-file.txt";

        // Ensure the bucket does not exist before the test
        if (minioService.bucketExists(bucketName)) {
            minioService.removeObject(bucketName, objectName);
        }

        // Upload the object, which should trigger bucket creation
        minioService.uploadObject(bucketName, objectName, filePath);

        // Verify the bucket was created
        boolean bucketExists = minioService.bucketExists(bucketName);
        assertEquals(true, bucketExists);

        // Clean up by removing the object and bucket
        minioService.removeObject(bucketName, objectName);
    }

    @Test
    void bucketExistsThrowsMinioServiceException() {
        String invalidBucketName = "invalidBucketName";

        MinioServiceException exception = assertThrows(
                MinioServiceException.class,
                () -> minioService.bucketExists(invalidBucketName));

        assertEquals("Failed to check bucket existence", exception.getMessage());
    }

    @Test
    void makeBucketThrowsMinioServiceException() {
        String invalidBucketName = "invalidBucketName";

        MinioServiceException exception = assertThrows(
                MinioServiceException.class,
                () -> minioService.makeBucket(invalidBucketName));

        assertEquals("Failed to create bucket", exception.getMessage());
    }
}