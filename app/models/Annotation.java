package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Annotation extends Model {

    @Column
    @Id
    @GeneratedValue
    public Long id;

    @Column
    @Formats.DateTime(pattern="dd/MM/yyyy")
    @CreatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone="GMT")
    public Date creationDate;

    @Column
    @Required
    @Constraints.MaxLength(128)
    public String annotation;

    @Column
    @ManyToMany
    public Collection<DagrComponent> annotatedDagrComponents;
}
