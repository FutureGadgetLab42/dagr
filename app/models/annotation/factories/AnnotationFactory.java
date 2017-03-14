package models.annotation.factories;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.AnnotationCreationException;
import models.annotation.Annotation;
import models.dagr.DagrComponent;
import play.Logger;

import java.util.Date;
import java.util.UUID;

public class AnnotationFactory {

    public Annotation buildAnnotation(JsonNode requestJson, DagrComponent componentToAnnotate) throws AnnotationCreationException {
        String annotationText = requestJson.findPath("annotationText").asText();

        if(annotationText == null) {
            Logger.warn("Bad request: " + requestJson);
            throw new AnnotationCreationException("Bad request: " + requestJson);
        } else {
            AnnotationBuilder annotationBuilder = new AnnotationBuilder();
            annotationBuilder.addComponent(componentToAnnotate);
            annotationBuilder.setCreationDate(new Date());
            Annotation annotationToAdd = annotationBuilder.build();
            componentToAnnotate.addAnnotation(annotationToAdd);
            return annotationToAdd;
        }
    }

}
