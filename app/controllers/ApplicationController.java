package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.http_methods.requests.*;
import controllers.http_methods.validators.RequestValidator;
import models.annotation.Annotation;
import models.dagr.Dagr;
import models.dagr.DagrComponent;
import models.dagr.factories.FactoryWrapper;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utilities.data_sources.DatabaseAccessor;
import utilities.exceptions.CreateComponentException;
import utilities.exceptions.DagrCreationException;
import utilities.exceptions.DeleteAnnotationException;
import utilities.exceptions.FindDagrByDateException;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ApplicationController extends Controller {

    private static final FactoryWrapper FACTORIES = new FactoryWrapper();
    private static final DatabaseAccessor DATABASE_ACCESSOR = new DatabaseAccessor();
    private static final RequestValidator REQUEST_VALIDATOR = new RequestValidator();

    @Inject
    private FormFactory FORM_FACTORY;


    /**************************************************************************************************
     * Web pages
     **************************************************************************************************/
    public Result index() {
        return ok(views.html.index.render("hi"));
    }

    public Result insertPage() {
        return ok(views.html.insert.render());
    }

    public Result queryPage() {
        return ok(views.html.query.render());
    }

    public Result findByUuidPage() {
        Form<FindDagrByUuidRequest> requestForm = FORM_FACTORY.form(FindDagrByUuidRequest.class);
        return ok(views.html.uuid.render(requestForm));
    }

    public Result findByDatePage() {
        Form<FindDagrByDateRequest> requestFrom = FORM_FACTORY.form(FindDagrByDateRequest.class);
        return ok(views.html.date.render(requestFrom));
    }

    public Result reachQueryPage() {
        return ok("ok");
    }

    public Result sterileReportPage() {
        return ok("ok");
    }

    public Result orphanReportPage() {
        return ok("ok");
    }

    /**************************************************************************************************
     * Query Requests
     **************************************************************************************************/

    /**
     * Find DAGR by UUID.
     * */
    @Transactional
    public Result findDagrByUuid() {
        Result response;
        Map<String, String[]> requestBody = request().body().asFormUrlEncoded();
        try {
            UUID dagrUuid = UUID.fromString(requestBody.get("uuid")[0]);
            Optional<Dagr> dagrOptional = DATABASE_ACCESSOR.findDagrByUuid(dagrUuid);

            if(dagrOptional.isPresent()) {
                response = ok(Json.toJson(dagrOptional.get()));
            } else {
                response = ok("DAGR not present: " + dagrUuid);
            }
        } catch(IllegalArgumentException e) {
            response = badRequest("Invalid request body.");
        }
        return response;
    }

    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    @Transactional
    public Result findDagrByDate() {
        Result response;
        try {

            FindDagrByDateRequest findDagrByDateRequest = REQUEST_VALIDATOR
                    .validateFindDagrByDateRequest(request().body().asFormUrlEncoded());

            Optional<List<Dagr>> dagrListOptional = DATABASE_ACCESSOR
                    .findDagrsByDate(findDagrByDateRequest.getStartDate(), findDagrByDateRequest.getEndDate());

            if(dagrListOptional.isPresent()) {
                response = ok(Json.toJson(dagrListOptional.get()));
            } else {
                response = ok("DAGRs between " + findDagrByDateRequest.getStartDate() + " to " +
                        findDagrByDateRequest.getEndDate() + " not present.");
            }

        } catch(FindDagrByDateException e) {
            response = badRequest("Invalid request");
        }

        return response;
    }

    @Transactional
    public Result findDagrByAuthor(String author) {
        Result response;

        Optional<List<Dagr>> dagrListOptional = DATABASE_ACCESSOR.findDagrsByAuthor(author);
        if(dagrListOptional.isPresent()) {
            response = ok(Json.toJson(dagrListOptional.get()));
        } else {
            response = ok("DAGRs with author " + author + " not present");
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

    //TODO this method!
    @Transactional
    public Result findDagrComponentByFileType(String fileType) {
        throw new RuntimeException("Not implemented");
    }

    //TODO
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result orphanReport() {
        throw new RuntimeException("Not yet implemented");
    }

    //TODO
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result sterileReport() {
        throw new RuntimeException("not yet implemented");
    }

    //TODO
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result reachQuery(UUID uuid) {
        throw new RuntimeException("not yet implemented");
    }


    /**************************************************************************************************
     * Creation Requests
     **************************************************************************************************/

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
     * Adding a DAGR component.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
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
     * Adding an annotation to the database.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
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
     * Batch add annotations to components.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result batchAddAnnotations() {
        throw new RuntimeException("not yet implemented");
    }

    @Transactional
    public Result upload() {
        Result response;
        Http.MultipartFormData<File> requestBody = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> mfdFile = requestBody.getFile("file");
        try {
            Map<String, String[]> formUrlEncoded = requestBody.asFormUrlEncoded();
            UUID parentUuid = UUID.fromString(formUrlEncoded.get("uuid")[0]);
            Optional<Dagr> parentDagr = DATABASE_ACCESSOR.findDagrByUuid(parentUuid);

            if(parentDagr.isPresent()) {
                CreateComponentRequest createComponentRequest = REQUEST_VALIDATOR.validateCreateComponentRequest(mfdFile);
                DagrComponent resultingComponent = FACTORIES.dagrComponentFactory.buildDagrComponent(createComponentRequest, parentDagr.get());
                response = ok(Json.toJson(resultingComponent));
            } else {
                response = badRequest("Invalid Parent DAGR UUID provided: " + parentUuid);
            }
        } catch(CreateComponentException e) {
            flash("error", "Missing file");
            response = badRequest("Missing File");
        } catch(IllegalArgumentException e) {
            flash("error", "Invalid parent UUID");
            response = badRequest("Invalid parent UUID");
        }
        return response;
    }


    /**************************************************************************************************
     * Deletion Requests
     **************************************************************************************************/

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
     * Deleting an annotation from a DAGR component.
     * */
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result deleteAnnotation() {
        Result response = null;

        JsonNode requestBody = request().body().asJson();
        try {
            DeleteAnnotationRequest deleteAnnotationRequest = REQUEST_VALIDATOR.validateDeleteAnnotationRequest(requestBody);
            DATABASE_ACCESSOR.deleteAnnotation(deleteAnnotationRequest.getUuidToDeleteFrom(), deleteAnnotationRequest.getAnnotation());
        } catch(DeleteAnnotationException e) {
            response = badRequest("Invalid request: " + requestBody);
            Logger.warn("Invalid delete annotation request: " + requestBody);
        }
        return response;
    }
}
