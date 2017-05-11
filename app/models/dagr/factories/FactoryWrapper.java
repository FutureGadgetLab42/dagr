package models.dagr.factories;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.http_methods.requests.CreateDagrRequest;
import models.annotation.Annotation;
import models.annotation.factories.AnnotationFactory;
import models.dagr.Dagr;

import java.util.Map;

public class FactoryWrapper {
    public DagrFactory dagrFactory;
    public AnnotationFactory annotationFactory;

    public FactoryWrapper() {
        dagrFactory = new DagrFactory();
        annotationFactory = new AnnotationFactory();
    }

    public Dagr buildDagr(CreateDagrRequest createDagrRequest) {
        return dagrFactory.buildDagr(createDagrRequest);
    }

    public Annotation buildAnnotation(Map<String, String[]> requestBody, Dagr dagrToAnnotate) {
        return annotationFactory.buildAnnotation(requestBody);
    }
}
