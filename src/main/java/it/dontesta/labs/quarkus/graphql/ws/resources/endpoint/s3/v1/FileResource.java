/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.resources.endpoint.s3.v1;

import it.dontesta.labs.quarkus.graphql.s3.service.MinioService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Map;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/s3/files")
public class FileResource {

    @Inject
    MinioService minioService;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(
            @RestForm("bucketName") @NotNull @NotEmpty String bucketName,
            @RestForm("objectName") @NotNull @NotEmpty String objectName,
            @RestForm("objectFile") FileUpload fileUpload) {

        try {
            minioService.uploadObject(bucketName, objectName,
                    fileUpload.filePath().toString());

            Map<String, Object> objectDetails = minioService.getObjectDetails(bucketName, objectName);
            return Response.ok(objectDetails).build();
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage());
        }
    }

    @GET
    @Path("/download/{bucketName}/{objectName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(
            @PathParam("bucketName") @NotNull @NotEmpty String bucketName,
            @PathParam("objectName") @NotNull @NotEmpty String objectName) {

        // Scarica il file da MinIO
        InputStream fileStream = minioService.getObject(bucketName, objectName);
        return Response.ok(fileStream)
                .header("Content-Disposition", "attachment; filename=\"" + objectName + "\"")
                .build();
    }
}