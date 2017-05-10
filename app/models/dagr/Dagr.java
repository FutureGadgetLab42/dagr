package models.dagr;

import models.annotation.Annotation;
import models.dagr.factories.DagrBuilder;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import utilities.Constants;

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

    @ManyToMany
    public Set<Dagr> linkedDagrs = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
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
        this.annotations.add(annotationToAdd);
    }
}
