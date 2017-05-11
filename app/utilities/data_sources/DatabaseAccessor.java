package utilities.data_sources;

import models.annotation.Annotation;
import models.dagr.Dagr;
import play.Logger;
import play.db.ebean.Transactional;

import javax.persistence.NonUniqueResultException;
import java.util.*;

public class DatabaseAccessor {

    /**
     * Lists all DAGRs contained in the database.
     *
     * @return result
     *              An optional containing the list of all present DAGRs.
     * */
    @Transactional
    public List<Dagr> listAllDagrs() {
        List<Dagr> result = Dagr.FIND.all();
        result.sort((a, b) -> a.creationDate.compareTo(b.creationDate));
        return result;
    }

    /**
     * Returns an Optional DAGR containing the unique DAGR associated with the given ID
     *
     * @param id
     *          The unique database ID of the DAGR
     * */
    @Transactional
    public Optional<Dagr> findDagrById(Long id) {
        Optional<Dagr> result;

        Dagr dagr = Dagr.FIND.byId(id);
        if(dagr == null) {
            Logger.debug("No DAGR found with ID: " + id);
            result = Optional.empty();
        } else {
            result = Optional.of(dagr);
        }
        return result;
    }

    /**
     * Returns an Optional DAGR containing the unique DAGR associated with the given UUID
     *
     * @param dagrUuid
     *          The unique UUID associated with the DAGR
     *
     * @throws NonUniqueResultException
     *          If multiple DAGRs are found to be associated with the given UUID.
     * */
    @Transactional
    public Optional<Dagr> findDagrByUuid(UUID dagrUuid) {
        Optional<Dagr> result;
        try {
            Dagr dagr = Dagr.FIND.where()
                    .like("dagrUuid", "%" + dagrUuid.toString() + "%")
                    .findUnique();
            if(dagr == null) result = Optional.empty(); else result = Optional.of(dagr);
        } catch(NonUniqueResultException nonUniqueResultException) {
            Logger.warn("Non-unique result found for UUID: " + dagrUuid);
            result = Optional.empty();
        }
        return result;
    }

    @Transactional
    public Optional<List<Dagr>> findDagrsByDate(Date startDate, Date endDate) {
        Optional<List<Dagr>> result;

        List<Dagr> dagrList = Dagr.FIND.where()
                .between("creationDate", startDate, endDate)
                .findList();

        if(dagrList == null) {
            Logger.info("Could not find DAGRs between " + startDate + " and " + endDate);
            result = Optional.empty();
        } else {
            dagrList.sort((a, b) -> a.creationDate.compareTo(b.creationDate));
            result = Optional.of(dagrList);
        }

        return result;
    }

    /**
     * Finds all DAGRs created on the given date
     *
     * @param date
     *          The desired creation date for DAGRs to return, sorted by ascending date
     * */
    @Transactional
    public Optional<List<Dagr>> findDagrsByDate(Date date) {
        Optional<List<Dagr>> result;

        List<Dagr> dagrList = Dagr.FIND.where()
                .between("dagrComponentCreationDate", date, date)
                .findList();

        if(dagrList == null) {
            Logger.info("Could not find DAGRs on date: " + date);
            result = Optional.empty();
        } else {
            dagrList.sort((a, b) -> a.creationDate.compareTo(b.creationDate));
            result = Optional.of(dagrList);
        }

        return result;
    }

    /**
     * Finds all DAGRs that contain the given annotationText.
     *
     * @param annotation
     *          The desired annotationText to search for.
     *
     * @return result
     *          An Optional containing the List of all DAGRs matching the given annotationText.
     * */
    @Transactional
    public Optional<List<Dagr>> findDagrsByAnnotation(String annotation) {
        Optional<List<Dagr>> result;

        List<Dagr> dagrList = Dagr.FIND.where()
                .like("annotation", "%" + annotation + "%")
                .findList();

        if(dagrList == null) {
            Logger.warn("Couldn't find DAGRs with annotation: " + annotation);
            result = Optional.empty();
        } else {
            result = Optional.of(dagrList);
        }

        return result;
    }

    @Transactional
    public Optional<List<Dagr>> findDagrsByAuthor(String author) {
        Optional<List<Dagr>> result;

        List<Dagr> dagrs = Dagr.FIND.where()
                .like("author", "%" + author + "%")
                .findList();

        if(dagrs == null) {
            result = Optional.empty();
        } else {
            result = Optional.of(dagrs);
        }

        return result;
    }

    @Transactional
    public Optional<Dagr> findDagrByName(String name) {
        Optional<Dagr> result;
        Dagr dagr = Dagr.FIND.where()
                .like("name", "%" + name + "%")
                .findUnique();

        if(dagr == null) {
            result = Optional.empty();
        } else {
            result = Optional.of(dagr);
        }

        return result;
    }

    @Transactional
    public List<Dagr> findDagrsByContentType(String contentType) {
        return Dagr.FIND.where()
                .like("contentType", "%" + contentType + "%")
                .findList();
    }

    /**
     * Finds all DAGRs that match any of the given annotations.
     *
     * @param annotationSet
     *          The set containing unique annotations to search for.
     *
     * @return result
     *          An Optional containing the List of all DAGRs that match
     *          at least one of the given annotations.
     * */
    @Transactional
    public Optional<List<Dagr>> batchFindByAnnotation(Set<String> annotationSet) {
        return null;
    }

    /**
     * Deletes the DAGR with the given UUID.
     *
     * @param dagrUuid
     *          The UUID of the DAGR to be deleted.
     * */
    @Transactional
    public boolean deleteDagrByUuid(UUID dagrUuid) {
        Optional<Dagr> dagrToDelete = findDagrByUuid(dagrUuid);

        if(dagrToDelete.isPresent()) {
            dagrToDelete.get().delete();
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Optional<Annotation> findAnnotationByUuid(UUID dagrUuid) {
        Optional<Annotation> result;

        try {
            Annotation annotation = Annotation.FIND.where()
                    .like("dagrUuid", "%" + dagrUuid + "%")
                    .findUnique();
            if(annotation == null) {
                result = Optional.empty();
            } else {
                result = Optional.of(annotation);
            }
        } catch(NonUniqueResultException e) {
            Logger.warn("Non-unique result found for UUID: " + dagrUuid);
            result = Optional.empty();
        }

        return result;
    }


    /**
     * Deletes the given annotation from the DAGR or DAGR component with the given UUID.
     *
     * @param uuid
     *              The UUID of the DAGR or DAGR component from which to delete the annotation
     *
     * @param annotation
     *              The annotation to be deleted.
     * */
    @Transactional
    public boolean deleteAnnotation(UUID uuid, String annotation) {
        boolean result;
        Optional<Annotation> annotationOptional = findAnnotationByUuid(uuid);

        if(annotationOptional.isPresent()) {
            annotationOptional.get().delete();
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    @Transactional
    public void renameDagr(UUID uuid, String name) {
        Optional<Dagr> dagrOptional = this.findDagrByUuid(uuid);
        if(dagrOptional.isPresent()) {
            Dagr dagr = dagrOptional.get();
            dagr.dagrName = name;
            dagr.update();
        }
    }

    /**
     * Inserts the given DAGR to the database
     *
     * @param dagr
     *          The DAGR to be inserted
     * */
    @Transactional
    public void saveDagr(Dagr dagr) {
        dagr.save();
        Logger.info("Successfully saved DAGR with UUID: " + dagr.dagrUuid);
    }

    @Transactional
    public void saveAnnotation(Annotation annotation) {
        annotation.save();
        Logger.info("Successfully saved annotation: " + annotation.annotationText);
    }

    @Transactional
    private boolean containsUniqueUuid(UUID uuid) {
        return findDagrByUuid(uuid).isPresent();
    }

    @Transactional
    public boolean containsDagrName(String dagrName) {
        return findDagrByName(dagrName).isPresent();
    }

}
