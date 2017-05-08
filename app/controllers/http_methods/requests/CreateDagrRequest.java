package controllers.http_methods.requests;

import java.util.Date;

public class CreateDagrRequest {

    private String documentName;
    private String resourceLocation;
    private Date documentCreationTime;
    private Date documentLastModifiedTime;
    private boolean containsDocument;

    public CreateDagrRequest(String documentName, String resourceLocation,
                             Date documentCreationTime, Date documentLastModifiedTime, boolean containsDocument) {
        setDocumentName(documentName);
        setResourceLocation(resourceLocation);
        setDocumentCreationTime(documentCreationTime);
        setDocumentLastModifiedTime(documentLastModifiedTime);
        setContainsDocument(containsDocument);
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

    public Date getDocumentCreationTime() {
        return documentCreationTime;
    }

    public void setDocumentCreationTime(Date documentCreationTime) {
        this.documentCreationTime = documentCreationTime;
    }

    public Date getDocumentLastModifiedTime() {
        return documentLastModifiedTime;
    }

    public void setDocumentLastModifiedTime(Date documentLastModifiedTime) {
        this.documentLastModifiedTime = documentLastModifiedTime;
    }

    public boolean containsDocument() {
        return containsDocument;
    }

    public void setContainsDocument(boolean containsDocument) {
        this.containsDocument = containsDocument;
    }
}
