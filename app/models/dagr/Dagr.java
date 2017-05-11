package models.dagr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.annotation.Annotation;
import models.dagr.factories.DagrBuilder;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.util.*;

@Entity
public class Dagr extends Model {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String dagrName;
    public String author;
    public String documentName;
    public String contentType;
    public String resourceLocation;
    public long fileSize;

    @Required
    public UUID dagrUuid;

    @DateTime(pattern="dd/MM/yyyy")
    @CreatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="GMT")
    public Date creationDate, documentCreationDate, lastModified;

    @JsonIgnore
    @ManyToOne
    public Dagr parentDagr;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentDagr")
    @JsonIgnore
    public Set<Dagr> childDagrs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    public Set<Annotation> annotations;

    public static final Find<Long, Dagr> FIND = new Find<Long, Dagr>(){};

    protected Dagr() {}

    public Dagr(DagrBuilder dagrBuilder) {
        this.creationDate = dagrBuilder.getDagrCreationDate();
        this.documentCreationDate = dagrBuilder.getDocumentCreationTime();
        this.dagrUuid = dagrBuilder.getDagrUuid();
        this.dagrName = dagrBuilder.getDagrName();
        this.contentType = dagrBuilder.getContentType();
        this.resourceLocation = dagrBuilder.getResourceLocation();
        this.documentName = dagrBuilder.getDocumentName();
        this.fileSize = dagrBuilder.getFileSize();
        this.lastModified = dagrBuilder.getLastModified();
        this.author = dagrBuilder.getAuthor();
    }

    public void addAnnotation(Annotation annotationToAdd) {
        this.getAnnotations().add(annotationToAdd);
    }

    public void addAdjacentDagr(Dagr adjacentDagr) {
        this.getChildDagrs().add(adjacentDagr);
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public Set<Dagr> getChildDagrs() {
        return childDagrs;
    }
}
