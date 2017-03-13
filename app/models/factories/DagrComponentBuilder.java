package models.factories;

import models.Dagr;
import models.DagrComponent;

import java.util.Date;
import java.util.UUID;

public class DagrComponentBuilder {

    private Date documentCreationDate, documentLastModifiedTime;
    private String documentName, resourceLocation, dagrComponentName;
    private UUID dagrComponentUuid;

    private Dagr parentDagr;

    public DagrComponent build() {
        return new DagrComponent(this);
    }

    public Date getDocumentCreationDate() {
        return documentCreationDate;
    }

    public void setDocumentCreationDate(Date documentCreationDate) {
        this.documentCreationDate = documentCreationDate;
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

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getDagrComponentName() {
        return dagrComponentName;
    }

    public void setDagrComponentName(String dagrComponentName) {
        this.dagrComponentName = dagrComponentName;
    }

    public UUID getDagrComponentUuid() {
        return dagrComponentUuid;
    }

    public void setDagrComponentUuid(UUID dagrComponentUuid) {
        this.dagrComponentUuid = dagrComponentUuid;
    }

    public Dagr getParentDagr() {
        return parentDagr;
    }

    public void setParentDagr(Dagr parentDagr) {
        this.parentDagr = parentDagr;
    }
}
