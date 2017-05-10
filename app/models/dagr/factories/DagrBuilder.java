package models.dagr.factories;

import models.dagr.Dagr;

import java.util.Date;
import java.util.UUID;

public class DagrBuilder {

    private Date dagrCreationDate, lastModified, documentCreationTime;
    private String dagrName;
    private String contentType;
    private String resourceLocation;
    private String documentName;
    private String author;
    private long fileSize;
    private UUID dagrUuid;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Date getDocumentCreationTime() {
        return documentCreationTime;
    }

    public void setDocumentCreationTime(Date documentCreationTime) {
        this.documentCreationTime = documentCreationTime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


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

