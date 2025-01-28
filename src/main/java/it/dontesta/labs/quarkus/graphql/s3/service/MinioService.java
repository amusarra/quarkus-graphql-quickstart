/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.s3.service;

import io.minio.PutObjectArgs;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import io.quarkus.logging.Log;
import it.dontesta.labs.quarkus.graphql.exception.MinioServiceException;
import org.jboss.resteasy.reactive.multipart.FileUpload;

/**
 * Service class for interacting with MinIO for file storage operations.
 */
@ApplicationScoped
public class MinioService {

    private final MinioClient minioClient;

    @Inject
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Uploads an object to a specified bucket in MinIO.
     *
     * @param bucketName the name of the bucket
     * @param objectName the name of the object
     * @param filePath the {@link FileUpload} to the file to be uploaded
     * @throws MinioServiceException if an error occurs during the upload
     */
    public void uploadObject(@NotEmpty @NotNull String bucketName,
            @NotEmpty @NotNull String objectName,
            @NotEmpty @NotNull String filePath) {

        Log.debugf("Uploading object '%s' with filePath '%s' to bucket '%s'...", objectName, filePath, bucketName);

        try {
            // Check if the file exists and is not empty
            long fileSize = Files.size(Paths.get(filePath));
            if (fileSize == 0) {
                throw new IllegalArgumentException("File is empty");
            }

            // Verify if the bucket exists, if not create it
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                Log.debugf("Created bucket '%s'", bucketName);
            }

            // Upload the object to MinIO
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(filePath)
                            .build());
            Log.debugf("Uploaded object '%s' to bucket '%s'", objectName, bucketName);
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
            throw new MinioServiceException("Failed to upload object to MinIO", e);
        }
    }

    /**
     * Overloaded method to upload an object to a specified bucket in MinIO using a byte array.
     *
     * @param bucketName the name of the bucket
     * @param objectName the name of the object
     * @param fileContent the byte array content of the file to be uploaded
     * @throws MinioServiceException if an error occurs during the upload
     */
    public void uploadObject(@NotEmpty @NotNull String bucketName,
            @NotEmpty @NotNull String objectName,
            @NotEmpty @NotNull byte[] fileContent) {

        Log.debugf("Uploading object '%s' with byte array content to bucket '%s'...", objectName, bucketName);

        try {
            // Verify if the bucket exists, if not create it
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                Log.debugf("Created bucket '%s'", bucketName);
            }

            // Upload the object to MinIO
            try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(inputStream, fileContent.length, -1)
                                .build());
            }
            Log.debugf("Uploaded object '%s' to bucket '%s'", objectName, bucketName);
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
            throw new MinioServiceException("Failed to upload object to MinIO", e);
        }
    }

    /**
     * Retrieves an object from a specified bucket in MinIO.
     *
     * @param bucketName the name of the bucket
     * @param objectName the name of the object
     * @return an InputStream to read the object
     * @throws MinioServiceException if an error occurs during the retrieval
     */
    public InputStream getObject(@NotEmpty @NotNull String bucketName, @NotEmpty @NotNull String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            throw new MinioServiceException("Failed to retrieve object details from MinIO", e);
        }
    }

    /**
     * Retrieves an object from a specified bucket in MinIO and returns it as a Base64 encoded string.
     *
     * @param bucketName the name of the bucket
     * @param objectName the name of the object
     * @return a Base64 encoded string of the object content
     * @throws MinioServiceException if an error occurs during the retrieval
     */
    public String getObjectAsBase64(@NotEmpty @NotNull String bucketName, @NotEmpty @NotNull String objectName) {
        try (InputStream inputStream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build())) {

            byte[] bytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            throw new MinioServiceException("Failed to retrieve object from MinIO", e);
        }
    }

    /**
     * Checks if a bucket exists in MinIO.
     *
     * @param bucketName the name of the bucket
     * @return true if the bucket exists, false otherwise
     * @throws MinioServiceException if an error occurs during the check
     */
    public boolean bucketExists(@NotEmpty @NotNull String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new MinioServiceException("Failed to check bucket existence", e);
        }
    }

    /**
     * Creates a new bucket in MinIO.
     *
     * @param bucketName the name of the bucket
     * @return true if the bucket was created successfully, false otherwise
     * @throws MinioServiceException if an error occurs during the creation
     */
    public boolean makeBucket(@NotEmpty @NotNull String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

            return bucketExists(bucketName);
        } catch (Exception e) {
            throw new MinioServiceException("Failed to create bucket", e);
        }
    }

    /**
     * Removes an object from a specified bucket in MinIO.
     *
     * @param bucketName the name of the bucket
     * @param objectName the name of the object
     * @throws MinioServiceException if an error occurs during the removal
     */
    public void removeObject(@NotEmpty @NotNull String bucketName, @NotEmpty @NotNull String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            throw new MinioServiceException("Failed to remove object from MinIO", e);
        }
    }

    /**
     * Retrieves the details of an object from a specified bucket in MinIO.
     *
     * @param bucketName the name of the bucket
     * @param objectName the name of the object
     * @return a map containing the details of the object
     * @throws MinioServiceException if an error occurs during the retrieval
     */
    public Map<String, Object> getObjectDetails(@NotEmpty @NotNull String bucketName, @NotEmpty @NotNull String objectName) {
        Map<String, Object> objectDetails = new HashMap<>();

        try {
            // Obtain the object details from MinIO
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            // Aggiungi i dettagli dell'oggetto alla mappa
            objectDetails.put("bucketName", bucketName);
            objectDetails.put("objectName", objectName);
            objectDetails.put("size", stat.size());
            objectDetails.put("contentType", stat.contentType());
            objectDetails.put("lastModified", stat.lastModified());
            objectDetails.put("etag", stat.etag());

            // Build the presigned URL for the object
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .build());

            objectDetails.put("downloadUrl", presignedUrl);

        } catch (Exception e) {
            throw new MinioServiceException("Failed to retrieve object details from MinIO", e);
        }

        return objectDetails;
    }
}