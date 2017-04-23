package models.dagr;

import models.dagr.factories.DagrBuilder;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import utilities.Constants;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Dagr extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @Required
    public UUID dagrUuid;

    @DateTime(pattern="dd/MM/yyyy")
    @CreatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="GMT")
    public Date creationDate;

    /** DAGR may be created with a document. */
    public UUID componentUuid;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = Constants.PARENT_DAGR)
    public List<DagrComponent> dagrComponentList;

    public static final Find<Long, Dagr> FIND = new Find<Long, Dagr>(){};

    protected Dagr() {}

    public Dagr(DagrBuilder dagrBuilder) {
        this.creationDate = dagrBuilder.getDagrCreationDate();
        this.dagrUuid = dagrBuilder.getDagrUuid();
    }

}
