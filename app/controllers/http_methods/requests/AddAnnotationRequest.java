package controllers.http_methods.requests;

import java.util.UUID;

public class AddAnnotationRequest {
    private String annotation;
    private UUID uuidToAnnotate;

    public AddAnnotationRequest(String annotation, UUID componentToAnnotate) {
        this.annotation = annotation;
        this.uuidToAnnotate = componentToAnnotate;
    }

    public UUID getUuidToAnnotate() {
        return uuidToAnnotate;
    }

    public void setUuidToAnnotate(UUID componentToAnnotate) {
        this.uuidToAnnotate = componentToAnnotate;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

}
