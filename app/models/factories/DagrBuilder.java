package models.factories;

import models.Dagr;

import java.util.Date;
import java.util.UUID;

public class DagrBuilder {
    private Date dagrCreationDate;
    private String dagrName;
    private UUID dagrUuid;

    public Dagr build() {
        return new Dagr(this);
    }

    public String getDagrName() {
        return dagrName;
    }

    public void setDagrName(String dagrName) {
        this.dagrName = dagrName;
    }

    protected void setDagrUuid(UUID dagrUuid) {
        this.dagrUuid = dagrUuid;
    }

    protected void setDagrCreationDate(Date dagrCreationDate) {
        this.dagrCreationDate = dagrCreationDate;
    }

    public Date getDagrCreationDate() {
        return dagrCreationDate;
    }

    public UUID getDagrUuid() {
        return dagrUuid;
    }


}

