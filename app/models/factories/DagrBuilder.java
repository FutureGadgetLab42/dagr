package models.factories;

import models.Dagr;

import java.util.Date;
import java.util.UUID;

public class DagrBuilder {
    private Date dagrCreationDate, documentLastModifiedTime, documentCreationDate;
    private String documentName, dagrName, resourceLocation;
    private UUID dagrUuid;

    public Dagr build() {
        return new Dagr(this);
    }

    public Date getDocumentLastModifiedTime() {
        return documentLastModifiedTime;
    }

    public void setDocumentLastModifiedTime(Date documentLastModifiedTime) {
        this.documentLastModifiedTime = documentLastModifiedTime;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDagrName() {
        return dagrName;
    }

    public void setDagrName(String dagrName) {
        this.dagrName = dagrName;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
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

    public Date getDocumentCreationDate() {
        return documentCreationDate;
    }

    public void setDocumentCreationDate(Date documentCreationDate) {
        this.documentCreationDate = documentCreationDate;
    }

}

