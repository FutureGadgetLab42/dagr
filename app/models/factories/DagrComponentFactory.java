package models.factories;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.DagrComponentCreationException;
import exceptions.DagrCreationException;
import models.Dagr;
import models.DagrComponent;
import play.Logger;

import java.util.Date;
import java.util.UUID;

public class DagrComponentFactory {

    public DagrComponent buildDagrComponent(JsonNode requestJson, Dagr parentDagr) throws DagrComponentCreationException {
        String documentName = requestJson.findPath("documentName").asText(),
                resourceLocation = requestJson.findPath("resourceLocation").asText(),
        dagrComponentName = requestJson.findPath("dagrComponentName").asText();
        Date documentCreationTime = new Date(requestJson.findPath("creationTime").asLong()),
                documentLastModifiedTime = new Date(requestJson.findPath("lastModified").asLong());

        if(documentName == null || resourceLocation == null || dagrComponentName == null
                || documentCreationTime == null || documentLastModifiedTime == null) {
            Logger.warn("Bad request: " + requestJson);
            throw new DagrCreationException("Bad request: " + requestJson);
        } else {
            return buildDagrComponent(dagrComponentName, documentName, resourceLocation, documentCreationTime, documentLastModifiedTime);
        }
    }

    private DagrComponent buildDagrComponent(String dagrComponentName, String documentName, String resourceLocation, Date documentCreationTime, Date documentLastModifiedTime) {
        DagrComponentBuilder dagrComponentBuilder = new DagrComponentBuilder();
        dagrComponentBuilder.setDagrComponentName(dagrComponentName);
        dagrComponentBuilder.setDocumentName(documentName);
        dagrComponentBuilder.setResourceLocation(resourceLocation);
        dagrComponentBuilder.setDocumentCreationDate(documentCreationTime);
        dagrComponentBuilder.setDocumentLastModifiedTime(documentLastModifiedTime);
        dagrComponentBuilder.setDagrComponentUuid(UUID.randomUUID());
        throw new RuntimeException();
    }
}
