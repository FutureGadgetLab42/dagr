package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import utilities.Constants;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;


@Entity
public class DagrComponent extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    @CreatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="GMT")
    public Date creationDate;

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
    @JoinColumn(name = Constants.DAGR_COMPONENT_UUID)
    public Collection<Annotation> annotations;

    public static final Find<Long, DagrComponent> FIND = new Find<Long, DagrComponent>() {};

    private DagrComponent(DagrComponentBuilder builder) {
        this.creationDate = builder.getCreationDate();
        this.lastModified = builder.getLastModified();
        this.resourceLocation = builder.getResourceLocation();
        this.dagrComponentUuid = builder.getDagrComponentUuid();
        this.parentDagr = builder.getParentDagr();
    }

    private static class DagrComponentBuilder {

        private Date creationDate, lastModified;
        private UUID dagrComponentUuid;
        private Dagr parentDagr;
        private String resourceLocation;

        private DagrComponentBuilder(Dagr parentDagr, String resourceLocation) {
            this.creationDate = new Date();
            this.lastModified = new Date();
            this.dagrComponentUuid = UUID.randomUUID();
            this.parentDagr = parentDagr;
            this.resourceLocation = resourceLocation;
        }

        private DagrComponent build() {
            return new DagrComponent(this);
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public Date getLastModified() {
            return lastModified;
        }

        public UUID getDagrComponentUuid() {
            return dagrComponentUuid;
        }

        public Dagr getParentDagr() {
            return parentDagr;
        }

        public String getResourceLocation() {
            return resourceLocation;
        }
    }
}

