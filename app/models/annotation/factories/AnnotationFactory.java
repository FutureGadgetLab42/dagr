package models.annotation.factories;

import com.fasterxml.jackson.databind.JsonNode;
import models.dagr.Dagr;
import utilities.exceptions.AnnotationCreationException;
import models.annotation.Annotation;
import play.Logger;

import java.util.Date;
import java.util.Map;

public class AnnotationFactory {

    public Annotation buildAnnotation(Map<String, String[]> requestBody) throws AnnotationCreationException {
        String annotationText = requestBody.get("annotation")[0];

        if(annotationText == null) {
            Logger.warn("Bad request: no annotation supplied.");
            throw new AnnotationCreationException("Bad request: no annotation supplied." );
        } else {
            AnnotationBuilder annotationBuilder = new AnnotationBuilder();
            annotationBuilder.setCreationDate(new Date());
            annotationBuilder.setAnnotationText(annotationText);
            Annotation annotationToAdd = annotationBuilder.build();
            return annotationToAdd;
        }
    }

}
