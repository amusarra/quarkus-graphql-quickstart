/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.app.lifecycle.s3;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import it.dontesta.labs.quarkus.graphql.s3.service.MinioService;

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
     * @param entityManager the EntityManager to be injected
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
     */
    void onBookFrontCoverUpload(@Observes StartupEvent event) {
        // This is the bucket name where the book front and back cover images will be uploaded
        // This value should be getting from the application configuration
        var bucketName = "book-cover";

        // List of book front and back cover image files
        // This file are located in the resources folder
        // See the quarkus.native.resources.includes configuration in the application.properties file
        // for the list of resources to be included in the native image
        List<String> imageFiles = List.of(
                "data/book/images/9780321967974_the_art_of_software_engineering_back_cover.jpg",
                "data/book/images/9780321967974_the_art_of_software_engineering_front_cover.jpg",
                "data/book/images/9780596805190_machine_learning_algorithms_back_cover.jpg",
                "data/book/images/9780596805190_machine_learning_algorithms_front_cover.jpg",
                "data/book/images/9780785316371_networked_neural_strategy_back_cover.jpg",
                "data/book/images/9780785316371_networked_neural_strategy_front_cover.jpg",
                "data/book/images/9780810885720_algorithmic_pattern_analysis_back_cover.jpg",
                "data/book/images/9780810885720_algorithmic_pattern_analysis_front_cover.jpg",
                "data/book/images/9781501582327_introduction_to_data_science_back_cover.jpg",
                "data/book/images/9781501582327_introduction_to_data_science_front_cover.jpg");

        List<String> uploadedFiles = new ArrayList<>();

        for (String imageFilePath : imageFiles) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imageFilePath)) {
                if (inputStream != null) {
                    String fileName = Paths.get(imageFilePath).getFileName().toString();
                    minioService.uploadObject(bucketName, fileName, inputStream.readAllBytes());
                    uploadedFiles.add(fileName);
                    Log.debugf("Uploaded book front and back cover image: {%s} to Minio into bucket name {%s}", fileName,
                            bucketName);
                } else {
                    Log.warnf("Resource file not found: {%s}", imageFilePath);
                }
            } catch (Exception e) {
                Log.errorf("Error uploading book front and back cover image: {%s} to Minio", imageFilePath, e);
            }
        }

        // Send the upload event with bucket name and list of uploaded files
        uploadEvent.fire(new UploadEvent(bucketName, uploadedFiles));
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

        Log.infof("Received upload event for bucket name {%s} with uploaded files: {%s} to update the related book", bucketName,
                uploadedFiles);

        Map<String, String> coverPairs = uploadedFiles.stream()
                .filter(fileName -> fileName.endsWith("front_cover.jpg") || fileName.endsWith("back_cover.jpg"))
                .collect(Collectors.groupingBy(
                        fileName -> fileName.replaceAll("_(front|back)_cover.jpg$", ""),
                        Collectors.mapping(fileName -> fileName, Collectors.toList())))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() == 2)
                .collect(Collectors.toMap(
                        entry -> entry.getValue().stream().filter(name -> name.endsWith("front_cover.jpg")).findFirst().orElseThrow(),
                        entry -> entry.getValue().stream().filter(name -> name.endsWith("back_cover.jpg")).findFirst().orElseThrow()));

        // Logic to update Book entities with front and back cover URLs
        coverPairs.forEach((frontCover, backCover) -> {
            var isbn = frontCover.split("_")[0];

            Book book = Book.findBookByQuery("isbn", isbn);

            if (book != null) {
                Map<String, Object> frontCoverDetails = minioService.getObjectDetails(bucketName, frontCover);
                Map<String, Object> backCoverDetails = minioService.getObjectDetails(bucketName, backCover);

                String frontCoverUrl = frontCoverDetails.get("downloadUrl").toString();
                String backCoverUrl = backCoverDetails.get("downloadUrl").toString();

                book.frontCoverImageUrl = frontCoverUrl;
                book.backCoverImageUrl = backCoverUrl;

                // Update the Book entity
                entityManager.merge(book);

                Log.debugf("Updated Book entity with ISBN {%s} with front cover URL {%s} and back cover URL {%s}", isbn,
                        frontCoverUrl, backCoverUrl);
            }
        });
    }

    /**
     * Event class to hold the upload event data.
     */
    public record UploadEvent(String bucketName, List<String> uploadedFiles) {
    }
}