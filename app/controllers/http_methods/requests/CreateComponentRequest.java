package controllers.http_methods.requests;

import java.util.Date;
import java.util.UUID;

public class CreateComponentRequest {
    private String fileName, contentType, resourceLocation;
    private Date lastModified;
    private long fileSize;
    private UUID parentDagrUuid;

    public UUID getParentDagrUuid() {
        return parentDagrUuid;
    }

    public void setParentDagrUuid(UUID parentDagrUuid) {
        this.parentDagrUuid = parentDagrUuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
