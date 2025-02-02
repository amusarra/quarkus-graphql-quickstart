/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.app.lifecycle.s3;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import it.dontesta.labs.quarkus.graphql.s3.service.MinioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class handles the uploading of book front and back cover images to Minio
 * from a specified resource folder.
 */
@ApplicationScoped
public class BookFrontBackCoverLoader {

    private final MinioService minioService;
    private final Event<UploadEvent> uploadEvent;
    private final EntityManager entityManager;

    /**
     * Constructor to inject the MinioService and Event.
     *
     * @param minioService the MinioService to be injected
     * @param uploadEvent the Event to be injected
     */
    @Inject
    public BookFrontBackCoverLoader(MinioService minioService, Event<UploadEvent> uploadEvent, EntityManager entityManager) {
        this.minioService = minioService;
        this.uploadEvent = uploadEvent;
        this.entityManager = entityManager;
    }

    /**
     * Event listener method that triggers on application startup to upload book
     * front and back cover images to Minio.
     *
     * @param event the startup event
     * @throws URISyntaxException if the resource URL is malformed
     */
    void onBookFrontCoverUpload(@Observes StartupEvent event) throws URISyntaxException {
        var resourceImageFolderPath = "data/book/images/";
        var bucketName = "book-cover";
        var resourseImageFolderUrl = getClass().getClassLoader().getResource(resourceImageFolderPath);

        if (resourseImageFolderUrl != null) {
            Log.info("Uploading book front and back cover images to Minio from resource folder: {%s}"
                    .formatted(resourseImageFolderUrl.toURI()));

            List<String> uploadedFiles = new ArrayList<>();

            if (resourseImageFolderUrl.getProtocol().equals("file")) {
                var resourceImageFolder = Paths.get(resourseImageFolderUrl.getPath());

                listAndUploadFiles(resourceImageFolder, bucketName, uploadedFiles);
            }

            if (resourseImageFolderUrl.getProtocol().equals("jar")) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(resourseImageFolderUrl.toURI(),
                        Collections.emptyMap())) {
                    var resourceImageFolder = fileSystem.getPath(resourceImageFolderPath);

                    listAndUploadFiles(resourceImageFolder, bucketName, uploadedFiles);
                } catch (Exception e) {
                    Log.error("Error accessing file system for resource folder", e);
                }
            }

            // Send the upload event with bucket name and list of uploaded files
            uploadEvent.fire(new UploadEvent(bucketName, uploadedFiles));
        }
    }

    /**
     * Event listener method to handle the upload event and update Book entities.
     *
     * @param event the upload event
     */
    @Transactional
    void onUploadEvent(@Observes UploadEvent event) {
        String bucketName = event.bucketName();
        List<String> uploadedFiles = event.uploadedFiles();

        Log.infof("Received upload event for bucket name {%s} with uploaded files: {%s} to update the relate book", bucketName,
                uploadedFiles);

        Map<String, String> coverPairs = uploadedFiles.stream()
                .filter(fileName -> fileName.endsWith("front_cover.jpg") || fileName.endsWith("back_cover.jpg"))
                .collect(Collectors.groupingBy(
                        fileName -> fileName.replaceAll("_(front|back)_cover.jpg$", ""),
                        Collectors.mapping(fileName -> fileName, Collectors.toList())))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() == 2)
                .collect(Collectors.toMap(
                        entry -> entry.getValue().stream().filter(name -> name.endsWith("front_cover.jpg")).findFirst()
                                .orElse(null),
                        entry -> entry.getValue().stream().filter(name -> name.endsWith("back_cover.jpg")).findFirst()
                                .orElse(null)));

        // Logic to update Book entities with front and back cover URLs
        coverPairs.forEach((frontCover, backCover) -> {
            var isbn = frontCover.split("_")[0];

            Book book = Book.find("isbn", isbn).firstResult();

            if (book != null) {
                Map<String, Object> frontCoverDetails = minioService.getObjectDetails(bucketName, frontCover);
                Map<String, Object> backCoverDetails = minioService.getObjectDetails(bucketName, backCover);

                String frontCoverUrl = frontCoverDetails.get("downloadUrl").toString();
                String backCoverUrl = backCoverDetails.get("downloadUrl").toString();

                book.frontCorverImageUrl = frontCoverUrl;
                book.backCorverImageUrl = backCoverUrl;

                // Update the Book entity
                entityManager.persist(book);

                Log.debugf("Updated Book entity with ISBN {%s} with front cover URL {%s} and back cover URL {%s}", isbn,
                        frontCoverUrl, backCoverUrl);
            }

        });
    }

    /**
     * Lists and uploads files from the specified resource folder to Minio.
     *
     * @param resourceImageFolder the path to the resource folder
     * @param bucketName the name of the Minio bucket
     * @param uploadedFiles the list to store the names of uploaded files
     */
    private void listAndUploadFiles(Path resourceImageFolder, String bucketName, List<String> uploadedFiles) {
        try (var filesStream = Files.list(resourceImageFolder)) {
            filesStream.forEach(file -> {
                if (Files.isRegularFile(file)) {
                    uploadFileToMinio(bucketName, file);
                    uploadedFiles.add(file.getFileName().toString());
                }
            });
        } catch (Exception e) {
            Log.error("Error listing files in resource folder", e);
        }
    }

    /**
     * Uploads a file to Minio.
     *
     * @param bucketName the name of the Minio bucket
     * @param file the path to the file to be uploaded
     */
    private void uploadFileToMinio(String bucketName, Path file) {
        try (InputStream inputStream = Files.newInputStream(file)) {
            minioService.uploadObject(bucketName, file.getFileName().toString(), inputStream.readAllBytes());
            Log.debugf("Uploaded book front and back cover image: {%s} to Minio into bucket name {%s}", file.getFileName(),
                    bucketName);
        } catch (Exception e) {
            Log.error("Error uploading book front and back cover image to Minio", e);
        }
    }

    /**
     * Event class to hold the upload event data.
     */
    private record UploadEvent(String bucketName, List<String> uploadedFiles) {
    }
}