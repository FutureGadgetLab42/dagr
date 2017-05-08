package models.dagr.factories;

import controllers.http_methods.requests.CreateDagrRequest;
import utilities.exceptions.DagrCreationException;
import models.dagr.Dagr;

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

    public Dagr buildDagr(CreateDagrRequest request) throws DagrCreationException {
        return buildDagr(request.getDocumentName(), request.getResourceLocation(),
                request.getDocumentCreationTime(), request.getDocumentLastModifiedTime());
    }

    private Dagr buildDagr(String documentName, String resourceLocation, Date documentCreationTime, Date documentLastModifiedTime) {
        DagrBuilder dagrBuilder = new DagrBuilder();
        dagrBuilder.setDagrCreationDate(new Date());
        dagrBuilder.setDagrUuid(UUID.randomUUID());
        return dagrBuilder.build();
    }
}
