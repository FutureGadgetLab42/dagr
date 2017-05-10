package controllers.http_methods.requests;

import java.util.UUID;

public class DeleteAnnotationRequest {

    private String annotation;
    private UUID uuidToDeleteFrom;

    public DeleteAnnotationRequest(String annotation, UUID uuid) {
        this.annotation = annotation;
        this.uuidToDeleteFrom = uuid;
    }

    public UUID getUuidToDeleteFrom() {
        return uuidToDeleteFrom;
    }

    public void setUuidToDeleteFrom(UUID uuidToDeleteFrom) {
        this.uuidToDeleteFrom = uuidToDeleteFrom;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }


}
