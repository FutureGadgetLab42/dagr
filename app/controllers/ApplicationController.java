package controllers;

import controllers.http_methods.requests.CreateDagrRequest;
import controllers.http_methods.validators.RequestValidator;
import models.annotation.Annotation;
import models.dagr.DagrComponent;
import com.fasterxml.jackson.databind.JsonNode;
import utilities.data_sources.DatabaseAccessor;
import utilities.exceptions.DagrCreationException;
import models.dagr.Dagr;
import models.dagr.factories.FactoryWrapper;
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

    private static final FactoryWrapper FACTORIES = new FactoryWrapper();
    private static final DatabaseAccessor DATABASE_ACCESSOR = new DatabaseAccessor();
    private static final RequestValidator REQUEST_VALIDATOR = new RequestValidator();

    public Result index() {
        return ok(views.html.index.render("hi"));
    }

    public Result insertPage() {
        return ok(views.html.insert.render());
    }

    public Result queryPage() {
        return ok(views.html.query.render());
    }

    /**
     * Inserts a new document as a new DAGR.
     * */
    @BodyParser.Of(BodyParser.class)
    @Transactional
    public Result insertNewDocumentAsNewDagr() {
        Result response;
        JsonNode requestBody = request().body().asJson();

        try {
            CreateDagrRequest createDagrRequest = REQUEST_VALIDATOR.validateCreateDagrRequest(requestBody);
            Dagr createdDagr = FACTORIES.dagrFactory.buildDagr(createDagrRequest);
            DATABASE_ACCESSOR.saveDagr(createdDagr);
            response = ok(Json.toJson(createdDagr));
        } catch(DagrCreationException e) {
            Logger.warn("Received invalid request: " + requestBody);
            response = badRequest("Invalid request: " + requestBody);
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
    public Result insertNewDocumentAsComponent() {
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
                    DagrComponent dagrComponentToAdd = FACTORIES.dagrComponentFactory.buildDagrComponent(requestBody, parentDagr);
                    DATABASE_ACCESSOR.saveComponent(dagrComponentToAdd);
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
        Result response;

        Optional<DagrComponent> componentToDeleteOptional = DATABASE_ACCESSOR.findComponentByUuid(componentUuid);
        if(componentToDeleteOptional.isPresent()) {
            componentToDeleteOptional.get().delete();
            Logger.info("Successfully deleted component with UUID: " + componentUuid);
            response = ok("Successfully deleted component with UUID: " + componentUuid);
        } else {
            Logger.info("Component does not exist: " + componentUuid);
            response = ok("Component does not exist: " + componentUuid);
        }
        return response;
    }


    /**
     * Find component by UUID.
     * */
    @Transactional
    public Result findComponentByUuid(UUID componentUuid){
        Result response;

        Optional<DagrComponent> dagrComponentOptional = DATABASE_ACCESSOR.findComponentByUuid(componentUuid);
        if(dagrComponentOptional.isPresent()) {
            response = ok(Json.toJson(dagrComponentOptional.get()));
        } else {
            response = ok("DAGR component not present: " + componentUuid);
        }

        return response;
    }

    /**************************************************************************
     * Annotation methods
     **************************************************************************/


    /**
     * Adding an annotation to the database.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.class)
    public Result addAnnotationToDagr() {
        Result response = null;
        JsonNode requestBody = request().body().asJson();
        String uuidText = requestBody.findPath("componentUuid").asText();
        if(uuidText == null) {
            Logger.warn("Attempted to add annotation to invalid UUID: " + uuidText);
            response = badRequest("Invalid UUID: " + uuidText);
        } else {
            try {
                UUID uuid = UUID.fromString(uuidText);
                Optional<DagrComponent> dagrComponentOptional = DATABASE_ACCESSOR.findComponentByUuid(uuid);
                if(dagrComponentOptional.isPresent()) {
                    DagrComponent dagrComponent = dagrComponentOptional.get();
                    Annotation annotation = FACTORIES.annotationFactory.buildAnnotation(requestBody, dagrComponent);
                    DATABASE_ACCESSOR.saveAnnotation(annotation);
                    DATABASE_ACCESSOR.saveComponent(dagrComponent);
                    response = ok(Json.toJson(annotation));
                } else {
                    Logger.info("Failed to find component with UUID: " + uuid);
                    response = ok("Component not found: " + uuid);
                }
            } catch(IllegalArgumentException e) {
                Logger.warn("Attempted to parse invalid UUID: " + uuidText);
                response = badRequest("Invalid UUID provided: " + uuidText);
            }
        }
        return response;
    }

    /**
     * Deleting an annotation from a DAGR component.
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
