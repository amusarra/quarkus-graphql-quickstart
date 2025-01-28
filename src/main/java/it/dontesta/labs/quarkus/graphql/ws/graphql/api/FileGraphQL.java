package it.dontesta.labs.quarkus.graphql.ws.graphql.api;

import io.smallrye.mutiny.Uni;
import it.dontesta.labs.quarkus.graphql.exception.MinioServiceException;
import it.dontesta.labs.quarkus.graphql.s3.service.MinioService;
import it.dontesta.labs.quarkus.graphql.ws.dto.FileDTO;
import jakarta.inject.Inject;
import java.util.Base64;
import java.util.Map;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.GraphQLException;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class FileGraphQL {

    @Inject
    MinioService minioService;

    @Query("getFile")
    @Description("Obtain a file from the S3 bucket")
    public Uni<FileDTO> getFile(@Name("objectName") String objectName,
            @Name("bucketName") String bucketName) {

        return Uni.createFrom().item(() -> {
            Map<String, Object> fileDetails = minioService.getObjectDetails(bucketName, objectName);
            String contentBase64 = minioService.getObjectAsBase64(bucketName, objectName);

            return FileDTO.withOptionalFields(
                    (String) fileDetails.get("objectName"),
                    (String) fileDetails.get("bucketName"),
                    (String) fileDetails.get("downloadUrl"),
                    contentBase64,
                    (String) fileDetails.get("contentType"),
                    (Long) fileDetails.get("size"),
                    (String) fileDetails.get("eTag"));
        }).onFailure(MinioServiceException.class).transform(e -> new GraphQLException(e.getMessage()));
    }

    @Mutation("uploadFile")
    @Description("Load a file into the S3 bucket")
    public Uni<FileDTO> uploadFile(@Name("objectName") String objectName,
            @Name("bucketName") String bucketName,
            @Name("content") String contentBase64) {

        byte[] content = Base64.getDecoder().decode(contentBase64);
        minioService.uploadObject(bucketName, objectName, content);

        return Uni.createFrom().item(() -> {
            Map<String, Object> fileDetails = minioService.getObjectDetails(bucketName, objectName);

            return FileDTO.withMandatoryFields(
                    (String) fileDetails.get("objectName"),
                    (String) fileDetails.get("bucketName"),
                    (String) fileDetails.get("downloadUrl"));
        });
    }

    @Mutation("deleteFile")
    @Description("Elimina un file dal bucket S3")
    public Uni<Boolean> deleteFile(@Name("objectName") String objectName,
            @Name("bucketName") String bucketName) {
        minioService.removeObject(bucketName, objectName);
        return Uni.createFrom().item(true);
    }
}