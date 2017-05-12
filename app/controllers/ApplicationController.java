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

    public Result batchInsertPage() {
        return ok(views.html.batchupload.render());
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

    public Result findByNamePage() {
        return ok(views.html.name.render());
    }

    public Result findByAuthorPage() {
        return ok(views.html.author.render());
    }

    public Result findByTypePage() {
        return ok(views.html.contenttype.render());
    }

    public Result reachQueryPage() {
        return ok(views.html.reach.render());
    }

    public Result renamePage() {
        return ok(views.html.rename.render());
    }

    public Result createAnnotationPage() {
        return ok(views.html.annotation.render());
    }

    public Result dagrDisplay(List<Dagr> dagrs) {
        return ok(views.html.dagrdisplay.render(dagrs));
    }

    public Result renameDagrPage() {
        return ok(views.html.rename.render());
    }

    /**************************************************************************************************
     * Query Requests
     **************************************************************************************************/

    @Transactional
    public Result findDagrByName() {
        Result response;
        String dagrName = request().body().asFormUrlEncoded().get("name")[0];
        List<Dagr> resultList = new ArrayList<>();
        Optional<Dagr> resultDagr = DATABASE_ACCESSOR.findDagrByName(dagrName);

        if(resultDagr.isPresent()) {
            resultList = Collections.singletonList(resultDagr.get());
        }

        return dagrDisplay(resultList);
    }

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
                response = dagrDisplay(Collections.singletonList(dagrOptional.get()));
            } else {
                response = dagrDisplay(new ArrayList<>());
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
                response = dagrDisplay(dagrListOptional.get());
            } else {
                response = dagrDisplay(new ArrayList<>());
            }

        } catch(FindDagrByDateException e) {
            response = badRequest("Invalid request");
        }

        return response;
    }

    @Transactional
    public Result findDagrByAuthor() {
        Result response;
        String author = request().body().asFormUrlEncoded().get("author")[0];
        Optional<List<Dagr>> dagrListOptional = DATABASE_ACCESSOR.findDagrsByAuthor(author);
        if(dagrListOptional.isPresent()) {
            response = dagrDisplay(dagrListOptional.get());
        } else {
            response = dagrDisplay(new ArrayList<>());
        }

        return response;
    }

    @Transactional
    public Result findDagrByContentType() {
        String fileType = request().body().asFormUrlEncoded().get("contentType")[0];
        return dagrDisplay(DATABASE_ACCESSOR.findDagrsByContentType(fileType));
    }

    @Transactional
    public Result findDagrByAnnotation() {
        return ok("ok");
    }

    @Transactional
    public Result orphanReport() {
        List<Dagr> allDagrs = DATABASE_ACCESSOR.listAllDagrs();
        Set<Dagr> allDagrSet = new HashSet<>();
        allDagrs.forEach(d -> allDagrSet.add(d));
        allDagrs.forEach(d -> {
            d.childDagrs.forEach(allDagrSet::remove);
        });
        return ok(views.html.dagrdisplay.render(new ArrayList<>(allDagrSet)));
    }

    @Transactional
    public Result sterileReport() {
        return ok(views.html.dagrdisplay.render(DATABASE_ACCESSOR.listAllDagrs().stream().filter(d -> d.childDagrs.size() == 0).collect(Collectors.toList())));
    }

    @Transactional
    public Result reachQuery() {
        Result response = null;

        Map<String, String[]> requestBody = request().body().asFormUrlEncoded();
        try{
            UUID uuid = UUID.fromString(requestBody.get("uuid")[0]);
            Optional<Dagr> dagrOptional = DATABASE_ACCESSOR.findDagrByUuid(uuid);
            if(dagrOptional.isPresent()) {
                response = ok(views.html.dagrdisplay.render(dagrBfs(dagrOptional.get())));
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
    private List<Dagr> dagrBfs(Dagr startDagr) {
        List<Dagr> result = new ArrayList<>();
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
        return ok(views.html.dagrdisplay.render(DATABASE_ACCESSOR.listAllDagrs()));
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
                    Annotation annotation = FACTORIES.annotationFactory.buildAnnotation(requestBody);
                    DATABASE_ACCESSOR.saveAnnotation(annotation);
                    dagr.addAnnotation(annotation);
                    dagr.update();
                    annotation = DATABASE_ACCESSOR.findAnnotationById(annotation.id).get();
                    annotation.annotatedDagrs.add(dagr);
                    annotation.update();
                    response = this.createAnnotationPage();
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
            String dagrName = formUrlEncoded.get("name")[0], author = formUrlEncoded.get("author")[0],
                    path = formUrlEncoded.get("path")[0];
            CreateDagrRequest createDagrRequest = REQUEST_VALIDATOR.validateCreateDagrRequest(mfdFile, dagrName, author, path);
            Dagr resultingDagr = FACTORIES.dagrFactory.buildDagr(createDagrRequest);
            DATABASE_ACCESSOR.saveDagr(resultingDagr);
            response = dagrDisplay(Collections.singletonList(resultingDagr));
        } catch(CreateComponentException e) {
            flash("error", "Missing file");
            response = badRequest("Missing File");
        }
        return response;
    }

    @Transactional
    public Result batchUpload() {
        Result response;
        Http.MultipartFormData<File> requestBody = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart<File>> filePartList = requestBody.getFiles();
        List<Dagr> responseList = new ArrayList<>();
        filePartList.forEach(fp -> {
            try {
                CreateDagrRequest createDagrRequest = REQUEST_VALIDATOR.validateCreateDagrRequest(fp);
                Dagr resultingDagr = FACTORIES.dagrFactory.buildDagr(createDagrRequest);
                DATABASE_ACCESSOR.saveDagr(resultingDagr);
                responseList.add(resultingDagr);
            } catch (CreateComponentException e) {
                Logger.warn("Invalid file: " + fp);
            }
        });
        return dagrDisplay(responseList);
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
                Dagr parentDagr = parentDagrOptional.get(), childDagr = childDagrOptional.get();
                if(parentDagr.childDagrs.stream().noneMatch(cd -> cd.dagrUuid.equals(childDagr.dagrUuid))) {
                    parentDagr.addAdjacentDagr(childDagr);
                    childDagr.parentDagr = parentDagr;
                    childDagr.update();
                    parentDagr.update();
                }
                response = this.linkPage();
            } else {
                response = badRequest("UUID not present");
            }
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid request to link DAGRs " + requestBody);
            response = badRequest("Invalid request");
        }

        return response;
    }

    @Transactional
    public Result renameDagr() {
        Result response;
        Map<String, String[]> requestBody = request().body().asFormUrlEncoded();
        UUID uuid = UUID.fromString(requestBody.get("uuid")[0]);
        String name = requestBody.get("name")[0];
        Optional<Dagr> toRenameOptional = DATABASE_ACCESSOR.findDagrByUuid(uuid);
        if(toRenameOptional.isPresent()) {
            Dagr toRename = toRenameOptional.get();
            toRename.dagrName = name;
            toRename.update();
            response = dagrDisplay(Collections.singletonList(toRename));
        } else {
            response = renameDagrPage();
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
