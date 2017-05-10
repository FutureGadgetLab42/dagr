package models.annotation.factories;

import models.annotation.Annotation;
import models.dagr.Dagr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnotationBuilder {

    private Date creationDate;
    private String annotationText;
    private List<Dagr> annotatedDagrs;

    public AnnotationBuilder(){
        this.annotatedDagrs = new ArrayList<>();
    }

    public Annotation build() {
        return new Annotation(this);
    }

    public void addComponent(Dagr dagr) {
        this.annotatedDagrs.add(dagr);
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

    public List<Dagr> getAnnotatedDagrs() {
        return annotatedDagrs;
    }

    public void setAnnotatedDagrs(List<Dagr> annotatedDagrs) {
        this.annotatedDagrs = annotatedDagrs;
    }
}
