package controllers;

import models.factories.DagrFactory;
import com.fasterxml.jackson.databind.JsonNode;
import data_sources.DatabaseAccessor;
import exceptions.BeaconCreationExcecption;
import exceptions.BeaconSearchException;
import exceptions.ConfigurationException;
import exceptions.DagrCreationException;
import models.Beacon;
import models.BeaconRendezvous;
import models.Dagr;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ApplicationController extends Controller {

    @Inject private static final DatabaseAccessor DATABASE_ACCESSOR = new DatabaseAccessor();
    private static final DagrFactory DAGR_FACTORY = new DagrFactory();
    private Result HOMEPAGE = ok("homepage");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Result index() {
        return HOMEPAGE;
    }

    /*************************************************************************
     * DAGR methods
     **************************************************************************/


    /**
     * Inserts a new document as a new DAGR.
     * */
    @BodyParser.Of(BodyParser.class)
    @Transactional
    public Result insertNewDocumentAsNewDagr() {
        Result response;
        JsonNode requestBody = request().body().asJson();

        if(requestBody == null) {
            Logger.error("Invalid request received " + request().body().asText());
            response = badRequest("Invalid request format: " + request().body());
        } else {
            try {
                Dagr createdDagr = DAGR_FACTORY.buildDagr(requestBody);
                DATABASE_ACCESSOR.saveDagr(createdDagr);
                response = ok(Json.toJson(createdDagr));
            } catch(DagrCreationException e) {
                Logger.warn("Received invalid request: " + requestBody);
                response = badRequest("Invalid request: " + requestBody);
            }
        }

        return response;
    }


    /**
     * Deleting a DAGR.
     * */
    @Transactional
    public Result deleteDagr(UUID dagrUuid) {
        Result response;
        if(DATABASE_ACCESSOR.deleteDagrByUuid(dagrUuid)) {
            Logger.info("Deleted DAGR with UUID: " + dagrUuid);
            response = ok("Deleted DAGR with UUID: " + dagrUuid);
        } else {
            Logger.info("Attempted to delete absent DAGR with UUID: " + dagrUuid);
            response = ok("DAGR not found: " + dagrUuid);
        }
        return response;
    }

    /**
     * Find DAGR by UUID.
     * */

    /**************************************************************************
     * DAGR component methods
     **************************************************************************/

    /**
     * Adding a DAGR component.
     * */

    /**
     * Deleting a DAGR component.
     * */

    /**
     * Find component by UUID.
     * */

    /**************************************************************************
     * Annotation methods
     **************************************************************************/


    /**
     * Adding an annotation to a DAGR.
     * */

    /**
     * Deleting an annotation from a DAGR.
     * */

    /**
     * Adding an annotation to a component.
     * */

    /**
     * Batch add annotations
     * */

    /**
     * Deleting an annotation from a component.
     * */

}
