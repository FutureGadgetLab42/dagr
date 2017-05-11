package models.annotation.factories;

import models.annotation.Annotation;
import models.dagr.Dagr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnotationBuilder {

    private Date creationDate;
    private String annotationText;

    public AnnotationBuilder(){

    }

    public Annotation build() {
        return new Annotation(this);
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

}
