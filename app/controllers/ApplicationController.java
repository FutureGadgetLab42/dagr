package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.http_methods.requests.*;
import controllers.http_methods.validators.RequestValidator;
import models.annotation.Annotation;
import models.dagr.Dagr;
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
import utilities.exceptions.AnnotationCreationException;
import utilities.exceptions.CreateComponentException;
import utilities.exceptions.DeleteAnnotationException;
import utilities.exceptions.FindDagrByDateException;

import javax.inject.Inject;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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

    public Result insertFilePage() {
        return ok(views.html.file.render());
    }

    public Result linkPage() {
        Form<LinkDagrRequest> requestForm = FORM_FACTORY.form(LinkDagrRequest.class);
        return ok(views.html.link.render(requestForm));
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
        return ok(views.html.reach.render());
    }

    public Result createAnnotationPage() {
        return ok(views.html.annotation.render());
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

    //TODO this method!
    @Transactional
    public Result findDagrtByFileType(String fileType) {
        throw new RuntimeException("Not implemented");
    }

    @Transactional
    public Result orphanReport() {
        List<Dagr> allDagrs = DATABASE_ACCESSOR.listAllDagrs();
        Set<Dagr> allDagrSet = new HashSet<>();
        allDagrs.forEach(d -> allDagrSet.add(d));
        allDagrs.forEach(d -> {
            d.childDagrs.forEach(c -> {
                allDagrSet.remove(c);
            });
        });
        return ok(Json.toJson(allDagrSet));
    }

    @Transactional
    public Result sterileReport() {
        return ok(Json.toJson(DATABASE_ACCESSOR.listAllDagrs().stream().filter(d -> d.childDagrs.size() == 0).toArray()));
    }

    @Transactional
    public Result reachQuery() {
        Result response = null;

        Map<String, String[]> requestBody = request().body().asFormUrlEncoded();
        try{
            UUID uuid = UUID.fromString(requestBody.get("uuid")[0]);
            Optional<Dagr> dagrOptional = DATABASE_ACCESSOR.findDagrByUuid(uuid);
            if(dagrOptional.isPresent()) {
                response = ok(Json.toJson(dagrBfs(dagrOptional.get())));
            } else {
                response = badRequest("UUID not present: " + uuid);
            }
        } catch (IllegalArgumentException e) {
            response = badRequest("Invalid UUID");
            Logger.warn("Attempted reach query with invalid request body: " + requestBody);
        }

        return response;
    }

    @Transactional
    private Set<Dagr> dagrBfs(Dagr startDagr) {
        Set<Dagr> result = new HashSet<>();
        PriorityQueue<Dagr> queue = new PriorityQueue<>();
        queue.add(startDagr);

        while(!queue.isEmpty()) {
            Dagr currentDagr = queue.poll();
            result.add(currentDagr);
            Set<Dagr> adjacentDagrs = currentDagr.childDagrs;

            adjacentDagrs.forEach(d -> {
                if(!result.contains(d)) {
                    queue.add(d);
                }
            });
        }

        return result;
    }

    @Transactional
    public Result listAll() {
        return ok(Json.toJson(DATABASE_ACCESSOR.listAllDagrs()));
    }

    /**************************************************************************************************
     * Creation Requests
     **************************************************************************************************/

    /**
     * Adding an annotation to the database.
     * */
    @Transactional
    public Result addAnnotation() {
        Result response;

        Map<String, String[]> requestBody = request().body().asFormUrlEncoded();
        try {
            REQUEST_VALIDATOR.validateAddAnnotationRequest(requestBody);
        } catch(AnnotationCreationException e) {
            Logger.warn("Invalid annotation request: " + requestBody);
            response = badRequest("Invalid annotation request");
        }

        String uuidText = requestBody.get("uuid")[0];

        String annotationText = requestBody.get("annotation")[0];
        if(annotationText == null) {
            response = badRequest("Must add an annotation.");
        } else {
            try {
                UUID uuid = UUID.fromString(uuidText);
                Optional<Dagr> dagrOptional = DATABASE_ACCESSOR.findDagrByUuid(uuid);
                if(dagrOptional.isPresent()) {
                    Dagr dagr = dagrOptional.get();
                    Annotation annotation = FACTORIES.annotationFactory.buildAnnotation(requestBody, dagr);
                    DATABASE_ACCESSOR.saveAnnotation(annotation);
                    dagr.addAnnotation(annotation);
                    DATABASE_ACCESSOR.saveDagr(dagr);
                    response = ok(Json.toJson(annotation));
                } else {
                    Logger.info("Failed to find component with UUID: " + uuid);
                    response = ok("Component not found: " + uuid);
                }
            } catch(IllegalArgumentException e) {
                Logger.warn("Attempted to add annotation with invalid uuid: " + uuidText);
                response = badRequest("Invalid UUID: " + uuidText);
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
            String dagrName = formUrlEncoded.get("name")[0], author = formUrlEncoded.get("author")[0];
            CreateDagrRequest createDagrRequest = REQUEST_VALIDATOR.validateCreateDagrRequest(mfdFile, dagrName, author);
            Dagr resultingDagr = FACTORIES.dagrFactory.buildDagr(createDagrRequest);
            DATABASE_ACCESSOR.saveDagr(resultingDagr);
            response = ok(Json.toJson(resultingDagr));
        } catch(CreateComponentException e) {
            flash("error", "Missing file");
            response = badRequest("Missing File");
        }
        return response;
    }

    @Transactional
    public Result linkDagrs() {
        Result response;
        Map<String, String[]> requestBody = request().body().asFormUrlEncoded();
        try {
            UUID parentUuid = UUID.fromString(requestBody.get("parentUuid")[0]);
            UUID childUuid = UUID.fromString(requestBody.get("childUuid")[0]);
            Optional<Dagr> parentDagrOptional = DATABASE_ACCESSOR.findDagrByUuid(parentUuid);
            Optional<Dagr> childDagrOptional = DATABASE_ACCESSOR.findDagrByUuid(childUuid);
            if(parentDagrOptional.isPresent() && childDagrOptional.isPresent()) {
                Dagr parentDagr = parentDagrOptional.get();
                parentDagr.addAdjacentDagr(childDagrOptional.get());
                DATABASE_ACCESSOR.saveDagr(parentDagr);
                response = ok("Parent: " + parentUuid + " child: " + childUuid);
            } else {
                response = badRequest("UUID not present");
            }
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid rqeuest to link DAGRs " + requestBody);
            response = badRequest("Invalid request");
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
     * Deleting an annotation from a DAGR.
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
