package controllers;

import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import models.DagrComponent;
import models.factories.DagrComponentFactory;
import models.factories.DagrFactory;
import com.fasterxml.jackson.databind.JsonNode;
import data_sources.DatabaseAccessor;
import exceptions.DagrCreationException;
import models.Dagr;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

public class ApplicationController extends Controller {

    @Inject private static final DatabaseAccessor DATABASE_ACCESSOR = new DatabaseAccessor();
    private static final DagrFactory DAGR_FACTORY = new DagrFactory();
    private static final DagrComponentFactory DAGR_COMPONENT_FACTORY = new DagrComponentFactory();
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
    @Transactional
    public Result findDagrByUuid(UUID dagrUuid) {
        Result response;

        Optional<Dagr> dagrOptional = DATABASE_ACCESSOR.findDagrByUuid(dagrUuid);
        if(dagrOptional.isPresent()) {
            response = ok(Json.toJson(dagrOptional.get()));
        } else {
            response = ok("DAGR not present: " + dagrUuid);
        }

        return response;
    }

    /**************************************************************************
     * DAGR component methods
     **************************************************************************/

    /**
     * Adding a DAGR component.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.class)
    public Result addDagrComponent() {
        Result response = null;
        JsonNode requestBody = request().body().asJson();

        if(requestBody == null) {
            Logger.warn("Invalid request: " + requestBody);
            response = badRequest("Invalid request: " + requestBody);
        } else {
            String uuidText = requestBody.findPath("parentDagrUuid").asText();

            try{
                UUID parentDagrUuid = UUID.fromString(uuidText);
                Optional<Dagr> parentDagrOptional = DATABASE_ACCESSOR.findDagrByUuid(parentDagrUuid);

                if(parentDagrOptional.isPresent()) {
                    Dagr parentDagr = parentDagrOptional.get();
                    DagrComponent dagrComponentToAdd = DAGR_COMPONENT_FACTORY.buildDagrComponent(requestBody, parentDagr);
                    DATABASE_ACCESSOR.addDagrComponent(dagrComponentToAdd);
                    response = ok(Json.toJson(dagrComponentToAdd));
                } else {
                    response = badRequest("Invalid request. Parent DAGR does not exist: " + parentDagrUuid);
                }
            } catch(IllegalArgumentException e) {
                response = badRequest("Invalid request. Invalid parent DAGR UUID: " + uuidText);
            }

        }

        return response;
    }

    /**
     * Deleting a DAGR component.
     * */
    @Transactional
    public Result deleteDagrComponent(UUID componentUuid) {
        throw new RuntimeException();
    }


    /**
     * Find component by UUID.
     * */
    @Transactional
    public Result findComponentByUuid(UUID componentUuid){
        throw new RuntimeException();
    }

    /**************************************************************************
     * Annotation methods
     **************************************************************************/


    /**
     * Adding an annotation to a DAGR.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.class)
    public Result addAnnotationToDagr() {
        throw new RuntimeException();
    }

    /**
     * Deleting an annotation from a DAGR or DAGR component.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.class)
    public Result deleteAnnotation() {
        throw new RuntimeException();
    }

    /**
     * Batch add annotations to components.
     *
     *
     * */
    @Transactional
    @BodyParser.Of(BodyParser.class)
    public Result batchAddAnnotations() {
        throw new RuntimeException();
    }

}
