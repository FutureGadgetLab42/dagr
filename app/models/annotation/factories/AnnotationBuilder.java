package models.annotation.factories;

import models.annotation.Annotation;
import models.dagr.DagrComponent;

import java.util.Date;
import java.util.List;

public class AnnotationBuilder {

    private Date creationDate;
    private String annotationText;
    private List<DagrComponent> annotatedComponents;

    public Annotation build() {
        return new Annotation(this);
    }

    public void addComponent(DagrComponent component) {
        this.annotatedComponents.add(component);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public void setAnnotationText(String annotationText) {
        this.annotationText = annotationText;
    }

    public List<DagrComponent> getAnnotatedComponents() {
        return annotatedComponents;
    }

    public void setAnnotatedComponents(List<DagrComponent> annotatedComponents) {
        this.annotatedComponents = annotatedComponents;
    }
}
