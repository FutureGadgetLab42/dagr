package utilities.data_sources.parser;

public class ParsedHtmlData {

    private String contentType;
    private String elementName;
    private String resourceLocation;
    private long size;

    public ParsedHtmlData(String contentType, String elementName, String resourceLocation, long size) {
        this.contentType = contentType;
        this.elementName = elementName;
        this.resourceLocation = resourceLocation;
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
