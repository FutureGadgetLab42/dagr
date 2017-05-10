package controllers.http_methods.requests;

import java.util.Date;

public class CreateDagrRequest {

    private String contentType;
    private String resourceLocation;
    private String documentName;
    private String author;
    private String dagrName;
    private Date lastModified;
    private Date documentCreationTime;
    private long fileSize;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public CreateDagrRequest(){}

    public CreateDagrRequest(String documentName, String resourceLocation, String contentType,
                             Date documentCreationTime, Date documentLastModifiedTime, long fileSize) {
        setDocumentName(documentName);
        setResourceLocation(resourceLocation);
        setContentType(contentType);
        setDocumentCreationTime(documentCreationTime);
        setLastModified(documentLastModifiedTime);
        setFileSize(fileSize);
    }

    public String getDagrName() {
        return dagrName;
    }

    public void setDagrName(String dagrName) {
        this.dagrName = dagrName;
    }

    public Date getDocumentCreationTime() {
        return documentCreationTime;
    }

    public void setDocumentCreationTime(Date documentCreationTime) {
        this.documentCreationTime = documentCreationTime;
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


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
