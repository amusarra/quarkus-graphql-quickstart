/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class FileDTOTest {

    @Test
    void withMandatoryFieldsCreatesFileDTO() {
        FileDTO fileDTO = FileDTO.withMandatoryFields("test-object", "test-bucket", "https://example.com");

        assertEquals("test-object", fileDTO.objectName());
        assertEquals("test-bucket", fileDTO.bucketName());
        assertEquals("https://example.com", fileDTO.url());
        assertEquals(null, fileDTO.content());
        assertEquals(null, fileDTO.contentType());
        assertEquals(null, fileDTO.size());
        assertEquals(null, fileDTO.eTag());
    }

    @Test
    void withOptionalFieldsCreatesFileDTO() {
        FileDTO fileDTO = FileDTO.withOptionalFields("test-object", "test-bucket", "https://example.com", "test content",
                "text/plain", 123L, "etag123");

        assertEquals("test-object", fileDTO.objectName());
        assertEquals("test-bucket", fileDTO.bucketName());
        assertEquals("https://example.com", fileDTO.url());
        assertEquals("test content", fileDTO.content());
        assertEquals("text/plain", fileDTO.contentType());
        assertEquals(123L, fileDTO.size());
        assertEquals("etag123", fileDTO.eTag());
    }

    @Test
    void withOptionalFieldsHandlesNullValues() {
        FileDTO fileDTO = FileDTO.withOptionalFields("test-object", "test-bucket", "https://example.com", null, null, null,
                null);

        assertEquals("test-object", fileDTO.objectName());
        assertEquals("test-bucket", fileDTO.bucketName());
        assertEquals("https://example.com", fileDTO.url());
        assertEquals(null, fileDTO.content());
        assertEquals(null, fileDTO.contentType());
        assertEquals(null, fileDTO.size());
        assertEquals(null, fileDTO.eTag());
    }
}