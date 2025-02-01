package it.dontesta.labs.quarkus.graphql.app.lifecycle.s3;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import it.dontesta.labs.quarkus.graphql.s3.service.MinioService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
public class BookFrontBackCoverLoader {

    private final MinioService minioService;

    @Inject
    public BookFrontBackCoverLoader(MinioService minioService) {
        this.minioService = minioService;
    }

    void onBookFrontCoverUpload(@Observes StartupEvent event) throws URISyntaxException {
        var resourceImageFolderPath = "data/book/images";
        var bucketName = "book-cover";
        var resourseImageFolderUrl = getClass().getClassLoader().getResource(resourceImageFolderPath);

        if (resourseImageFolderUrl != null) {
            var resourceImageFolder = Paths.get(resourseImageFolderUrl.toURI());
            Log.info("Uploading book front and back cover images to Minio from resource folder: {%s}"
                    .formatted(resourceImageFolder));

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
    }

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