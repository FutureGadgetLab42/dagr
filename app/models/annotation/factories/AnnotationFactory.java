package models.annotation.factories;

import com.fasterxml.jackson.databind.JsonNode;
import models.dagr.Dagr;
import utilities.exceptions.AnnotationCreationException;
import models.annotation.Annotation;
import play.Logger;

import java.util.Date;

public class AnnotationFactory {

    public Annotation buildAnnotation(JsonNode requestJson, Dagr dagrToAnnotate) throws AnnotationCreationException {
        String annotationText = requestJson.findPath("annotationText").asText();

        if(annotationText == null) {
            Logger.warn("Bad request: " + requestJson);
            throw new AnnotationCreationException("Bad request: " + requestJson);
        } else {
            AnnotationBuilder annotationBuilder = new AnnotationBuilder();
            annotationBuilder.addComponent(dagrToAnnotate);
            annotationBuilder.setCreationDate(new Date());
            Annotation annotationToAdd = annotationBuilder.build();
            dagrToAnnotate.addAnnotation(annotationToAdd);
            return annotationToAdd;
        }
    }

}
