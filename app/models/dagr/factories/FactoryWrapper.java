package models.dagr.factories;

import controllers.http_methods.requests.CreateComponentRequest;
import controllers.http_methods.requests.CreateDagrRequest;
import play.data.Form;
import play.data.FormFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utilities.exceptions.DagrCreationException;
import models.annotation.factories.AnnotationFactory;
import models.dagr.Dagr;
import models.dagr.DagrComponent;

import javax.inject.Inject;

public class FactoryWrapper {
    public DagrFactory dagrFactory;
    public DagrComponentFactory dagrComponentFactory;
    public AnnotationFactory annotationFactory;

//    @Inject public FormFactory formFactory;
//    public Form<Dagr> dagrForm = formFactory.form(Dagr.class);
//    public Form<DagrComponent> dagrComponentForm = formFactory.form(DagrComponent.class);

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
        throw new NotImplementedException();
    }

}
