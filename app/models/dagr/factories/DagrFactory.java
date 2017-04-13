package models.dagr.factories;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.DagrCreationException;
import models.dagr.Dagr;
import play.Logger;

import java.util.Date;
import java.util.UUID;

/**
 * Responsible for de-serializing JSON from request bodies in to Models.
 *
 * Utilizes a combination of Factory and Builder design patterns to control
 * conditions under which Dagrs and DagrComponents can be instantiated to increase
 * database security and integrity guarantees.
 * */
public class DagrFactory {

    public Dagr     buildDagr(JsonNode requestJson) throws DagrCreationException {
        String documentName = requestJson.findPath("documentName").asText(),
                resourceLocation = requestJson.findPath("resourceLocation").asText();

        Date documentCreationTime = new Date(requestJson.findPath("creationTime").asLong()),
                documentLastModifiedTime = new Date(requestJson.findPath("lastModified").asLong());

        if(documentName == null || resourceLocation == null
                || documentCreationTime == null || documentLastModifiedTime == null) {
            Logger.warn("Bad request: " + requestJson);
            throw new DagrCreationException("Bad request: " + requestJson);
        } else {
            return buildDagr(documentName, resourceLocation, documentCreationTime, documentLastModifiedTime);
        }
    }

    private Dagr buildDagr(String documentName, String resourceLocation, Date documentCreationTime, Date documentLastModifiedTime) {
        DagrBuilder dagrBuilder = new DagrBuilder();
        dagrBuilder.setDagrCreationDate(new Date());
        dagrBuilder.setDagrUuid(UUID.randomUUID());
        return dagrBuilder.build();
    }





}
