/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.exception;

/**
 * Custom exception class for handling MinIO service errors.
 */
public class MinioServiceException extends RuntimeException {

  /**
   * Constructs a new MinioServiceException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public MinioServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}