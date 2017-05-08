package models.dagr;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import models.annotation.Annotation;
import models.dagr.factories.DagrComponentBuilder;
import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import utilities.Constants;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
public class DagrComponent extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    @CreatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="GMT")
    public Date dagrComponentCreationDate;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="GMT")
    public Date lastModified;

    @Required
    public UUID dagrComponentUuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = Constants.DAGR_UUID)
    public Dagr parentDagr;

    @Required
    public String resourceLocation;

    @ManyToMany(fetch = FetchType.LAZY)
    public List<Annotation> annotations;

    public static final Find<Long, DagrComponent> FIND = new Find<Long, DagrComponent>() {};

    public DagrComponent(DagrComponentBuilder builder) {
        this.dagrComponentCreationDate = builder.getDocumentCreationDate();
        this.lastModified = builder.getDocumentLastModifiedTime();
        this.resourceLocation = builder.getResourceLocation();
        this.dagrComponentUuid = builder.getDagrComponentUuid();
        this.parentDagr = builder.getParentDagr();
    }

    public void addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
    }
}

