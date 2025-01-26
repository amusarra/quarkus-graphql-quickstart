/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.s3.service;

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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MinioService {

    @Inject
    MinioClient minioClient;

    public void uploadObject(String bucketName, String objectName, String filePath) throws Exception {
        // Verifica se il bucket esiste, altrimenti lo crea
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // Carica il file
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(filePath)
                        .build());
    }

    public InputStream getObject(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file from MinIO", e);
        }
    }

    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public boolean makeBucket(String bucketName) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

        return bucketExists(bucketName);
    }

    public void removeObject(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public Map<String, Object> getObjectDetails(String bucketName, String objectName) {
        Map<String, Object> objectDetails = new HashMap<>();

        try {
            // Ottieni i metadati dell'oggetto
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
            // Aggiungi i dettagli dell'oggetto alla mappa
            objectDetails.put("bucketName", bucketName);
            objectDetails.put("objectName", objectName);
            objectDetails.put("size", stat.size());
            objectDetails.put("contentType", stat.contentType());
            objectDetails.put("lastModified", stat.lastModified());
            objectDetails.put("etag", stat.etag());

            // Genera un URL pre-signed per il download
            String presignedUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );

            objectDetails.put("downloadUrl", presignedUrl);

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve object details from MinIO", e);
        }

        return objectDetails;
    }

}