package it.dontesta.labs.quarkus.graphql.app.lifecycle.s3;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import it.dontesta.labs.quarkus.graphql.s3.service.MinioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * This class handles the uploading of book front and back cover images to Minio
 * from a specified resource folder.
 */
@ApplicationScoped
public class BookFrontBackCoverLoader {

    private final MinioService minioService;

    /**
     * Constructor to inject the MinioService.
     *
     * @param minioService the MinioService to be injected
     */
    @Inject
    public BookFrontBackCoverLoader(MinioService minioService) {
        this.minioService = minioService;
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

            if (resourseImageFolderUrl.getProtocol().equals("file")) {
                var resourceImageFolder = Paths.get(resourseImageFolderUrl.getPath());

                listAndUploadFiles(resourceImageFolder, bucketName);
            }

            if (resourseImageFolderUrl.getProtocol().equals("jar")) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(resourseImageFolderUrl.toURI(),
                        Collections.emptyMap())) {
                    var resourceImageFolder = fileSystem.getPath(resourceImageFolderPath);

                    listAndUploadFiles(resourceImageFolder, bucketName);
                } catch (Exception e) {
                    Log.error("Error accessing file system for resource folder", e);
                }
            }
        }
    }

    /**
     * Lists and uploads files from the specified resource folder to Minio.
     *
     * @param resourceImageFolder the path to the resource folder
     * @param bucketName the name of the Minio bucket
     */
    private void listAndUploadFiles(Path resourceImageFolder, String bucketName) {
        try (var filesStream = Files.list(resourceImageFolder)) {
            filesStream.forEach(file -> {
                if (Files.isRegularFile(file)) {
                    uploadFileToMinio(bucketName, file);
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
}