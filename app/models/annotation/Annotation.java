package models.annotation;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import models.annotation.factories.AnnotationBuilder;
import models.dagr.Dagr;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Annotation extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    @CreatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="GMT")
    public Date creationDate;

    @Required
    public String annotationText;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "annotations")
    @JsonBackReference
    public List<Dagr> annotatedDagrs;

    public static final Find<Long, Annotation> FIND = new Find<Long, Annotation>(){};

    protected Annotation() {}

    public Annotation(AnnotationBuilder annotationBuilder) {
        this.creationDate = annotationBuilder.getCreationDate();
        this.annotationText = annotationBuilder.getAnnotationText();
    }
}
