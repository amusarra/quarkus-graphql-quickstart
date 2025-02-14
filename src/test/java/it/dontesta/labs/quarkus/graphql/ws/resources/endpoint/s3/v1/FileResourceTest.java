/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.s3.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import it.dontesta.labs.quarkus.graphql.s3.service.MinioService;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.nio.file.Path;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileResourceTest {

    @Mock
    MinioService minioService;

    @InjectMocks
    FileResource fileResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFileSuccessfully() throws Exception {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        FileUpload fileUpload = mock(FileUpload.class);
        when(fileUpload.filePath()).thenReturn(Path.of("test-file-path"));

        Map<String, Object> objectDetails = new HashMap<>();
        objectDetails.put("key", "value");
        when(minioService.getObjectDetails(bucketName, objectName)).thenReturn(objectDetails);

        Response response = fileResource.uploadFile(bucketName, objectName, fileUpload);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(objectDetails, response.getEntity());
        verify(minioService).uploadObject(bucketName, objectName, "test-file-path");
    }

    @Test
    void uploadFileThrowsException() throws Exception {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        FileUpload fileUpload = mock(FileUpload.class);
        when(fileUpload.filePath()).thenReturn(Path.of("test-file-path"));

        doThrow(new RuntimeException("Upload failed")).when(minioService).uploadObject(bucketName, objectName,
                "test-file-path");

        assertThrows(WebApplicationException.class, () -> fileResource.uploadFile(bucketName, objectName, fileUpload));
    }

    @Test
    void downloadFileSuccessfully() {
        String bucketName = "test-bucket";
        String objectName = "test-object";
        InputStream fileStream = new ByteArrayInputStream("file-content".getBytes());
        when(minioService.getObject(bucketName, objectName)).thenReturn(fileStream);

        Response response = fileResource.downloadFile(bucketName, objectName);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(fileStream, response.getEntity());
        assertEquals("attachment; filename=\"" + objectName + "\"", response.getHeaderString("Content-Disposition"));
    }

    @Test
    void downloadFileThrowsException() {
        String bucketName = "test-bucket";
        String objectName = "test-object";

        doThrow(new WebApplicationException("Download failed")).when(minioService).getObject(bucketName, objectName);

        assertThrows(WebApplicationException.class, () -> fileResource.downloadFile(bucketName, objectName));
    }
}