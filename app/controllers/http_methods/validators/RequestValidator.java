package controllers.http_methods.validators;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.http_methods.requests.*;
import utilities.exceptions.DagrCreationException;
import play.Logger;

import java.util.Date;

public class RequestValidator {

    public AddAnnotationRequest validateAddAnnotationRequest(JsonNode requestBody) throws DagrCreationException {
        throw new RuntimeException();
    }

    public BatchAddAnnotationsRequest validateBatchAddAnnotationsRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public CreateComponentRequest validateCreateComponentRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public CreateDagrRequest validateCreateDagrRequest(JsonNode requestBody) {
        if(requestBody == null) {
            Logger.warn("No request body provided");
            throw new DagrCreationException("No request body provided");
        }

        String documentName = requestBody.findPath("documentName").asText(),
                resourceLocation = requestBody.findPath("resourceLocation").asText();
        Date documentCreationTime = new Date(requestBody.findPath("creationTime").asLong()),
                documentLastModifiedTime = new Date(requestBody.findPath("lastModified").asLong());
        Boolean containsDocument = requestBody.findPath("containsDocument").asBoolean();

        if(documentName == null || resourceLocation == null
                || documentCreationTime == null || documentLastModifiedTime == null) {
            Logger.warn("Bad request: " + requestBody);
            throw new DagrCreationException("Bad request: " + requestBody);
        } else {
            return new CreateDagrRequest(documentName, resourceLocation, documentCreationTime,
                    documentLastModifiedTime, containsDocument);
        }
    }

    public CreateDagrWithDocumentRequest validateCreateDagrWithDocumentRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public DeleteAnnotationRequest validateDeleteAnnotationRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public DeleteComponentRequest validateDeleteComponentRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public DeleteDagrRequest validateDeleteDagrRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public FindComponentByUuidRequest validateFindComponentByUuidRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public FindDagrByUuidRequest validateFindDagrbyUuidRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

}
