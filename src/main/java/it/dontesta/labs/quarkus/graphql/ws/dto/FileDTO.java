/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.dto;

import org.eclipse.microprofile.graphql.Description;

@Description("Rappresenta un file con i dettagli")
public record FileDTO(String objectName, String bucketName, String url, String content, String contentType, Long size,
        String eTag) {
    public static FileDTO withMandatoryFields(String objectName, String bucketName, String url) {
        return new FileDTO(objectName, bucketName, url, null, null, null, null);
    }

    public static FileDTO withOptionalFields(String objectName, String bucketName, String url, String content,
            String contentType,
            Long size, String eTag) {
        return new FileDTO(objectName, bucketName, url, content, contentType, size, eTag);
    }
}