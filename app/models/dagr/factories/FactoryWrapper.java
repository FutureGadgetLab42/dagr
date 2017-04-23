package models.dagr.factories;

import controllers.requests.CreateComponentRequest;
import controllers.requests.CreateDagrRequest;
import exceptions.DagrCreationException;
import models.annotation.factories.AnnotationFactory;
import models.dagr.Dagr;
import models.dagr.DagrComponent;

public class FactoryWrapper {
    public DagrFactory dagrFactory;
    public DagrComponentFactory dagrComponentFactory;
    public AnnotationFactory annotationFactory;

    public FactoryWrapper() {
        dagrFactory = new DagrFactory();
        dagrComponentFactory = new DagrComponentFactory();
        annotationFactory = new AnnotationFactory();
    }

    public Dagr buildDagr(CreateDagrRequest createDagrRequest) throws DagrCreationException {

        if(createDagrRequest.containsDocument()) {

        }

        return dagrFactory.buildDagr(createDagrRequest);
    }

    public DagrComponent buildDagrComponent(CreateComponentRequest createComponentRequest) {

    }

}
