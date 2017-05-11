package controllers.http_methods.validators;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.http_methods.requests.*;
import models.annotation.Annotation;
import models.dagr.Dagr;
import play.libs.Json;
import play.mvc.Http;
import utilities.data_sources.DatabaseAccessor;
import utilities.exceptions.*;
import play.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RequestValidator {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public AddAnnotationRequest validateAddAnnotationRequest(Map<String, String[]> requestBody) throws DagrCreationException {
        AddAnnotationRequest result = new AddAnnotationRequest();

        String uuidText = requestBody.get("uuid")[0];
        String annotationText = requestBody.get("annotation")[0];
        if(annotationText == null) {
            throw new AnnotationCreationException("Must give an annotation");
        } else {
            try {
                UUID uuid = UUID.fromString(uuidText);
                result.setAnnotation(annotationText);
                result.setUuidToAnnotate(uuid);
                return result;
            } catch(IllegalArgumentException e) {
                Logger.warn("Attempted to add annotation with invalid uuid: " + uuidText);
                throw new AnnotationCreationException("Attempted to add annotation with invalid uuid: " + uuidText);
            }
        }
    }

    public CreateDagrRequest validateCreateDagrRequest(Http.MultipartFormData.FilePart<File> filePart) {
        return validateCreateDagrRequest(filePart, "", "", "");
    }

    public CreateDagrRequest validateCreateDagrRequest(Http.MultipartFormData.FilePart<File> filePart, String name, String author, String path) {
        if(filePart != null) {
            CreateDagrRequest result = new CreateDagrRequest();
            File file = filePart.getFile();
            result.setDagrName(name);
            result.setDocumentName(filePart.getFilename());
            result.setContentType(filePart.getContentType());
            result.setResourceLocation(path);
            result.setDocumentCreationTime(new Date(file.lastModified()));
            result.setLastModified(result.getDocumentCreationTime());
            result.setFileSize(file.getTotalSpace());
            result.setAuthor(author);
            return result;
        } else {
            Logger.warn("Invalid or missing file upload attempted.");
            throw new CreateComponentException("Invalid file");
        }
    }

    public DeleteAnnotationRequest validateDeleteAnnotationRequest(JsonNode requestBody) {
        if(requestBody == null) {
            Logger.warn("No request body provided");
            throw new DeleteAnnotationException("No request body provided");
        }

        String annotation = requestBody.findPath("annotation").asText(),
                uuidText = requestBody.findPath("uuidToDeleteFrom").asText();

        try {
            UUID uuidToDeleteFrom = UUID.fromString(uuidText);
            return new DeleteAnnotationRequest(annotation, uuidToDeleteFrom);
        } catch(IllegalArgumentException e) {
            Logger.warn("No element found with UUID: " + uuidText);
            throw new DeleteAnnotationException("No element found with UUID: " + uuidText);
        }
    }

    public DeleteDagrRequest validateDeleteDagrRequest(JsonNode requestBody) {
        throw new RuntimeException();
    }

    public FindDagrByUuidRequest validateFindDagrbyUuidRequest(String requestBody) {
        try {
            FindDagrByUuidRequest result = new FindDagrByUuidRequest();
            result.setDagrUuid(UUID.fromString(requestBody));
            return result;
        } catch(IllegalArgumentException e) {
            throw new FindDagrByUuidException("Invalid UUID: " + requestBody);
        }
    }

    public FindDagrByDateRequest validateFindDagrByDateRequest(Map<String, String[]> requestBody) {
        try {
            FindDagrByDateRequest result = new FindDagrByDateRequest();
            result.setStartDate(DATE_FORMAT.parse(requestBody.get("startDate")[0]));
            result.setEndDate(DATE_FORMAT.parse(requestBody.get("endDate")[0]));
            return result;
        } catch(ParseException e) {
            throw new FindDagrByDateException("Invalid request body.");
        }
    }

}
