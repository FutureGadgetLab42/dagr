package utilities.data_sources;

import models.annotation.Annotation;
import models.dagr.Dagr;
import models.dagr.DagrComponent;
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
    public Optional<List<Dagr>> listAllDagrs() {
        Optional<List<Dagr>> result;
        List<Dagr> dagrList = Dagr.FIND.all();

        if(dagrList == null) {
            result = Optional.empty();
            Logger.info("Attempted to list all DAGRs while database is empty.");
        } else {
            dagrList.sort((a, b) -> a.creationDate.compareTo(b.creationDate));
            result = Optional.of(dagrList);
        }

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

    /**
     * Finds all DAGRs created on the given date
     *
     * @param date
     *          The desired creation date for DAGRs to return, sorted by ascending date
     * */
    @Transactional
    public  Optional<List<Dagr>> findDagrsByDate(Date date) {
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
        Optional<List<Dagr>> result = null;
        return result;
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
    public Optional<DagrComponent> findComponentByUuid(UUID componentUuid) {
        Optional<DagrComponent> result;

        DagrComponent dagrComponent = DagrComponent.FIND
                .where()
                .like("dagrComponentUuid", "%" + componentUuid + "%")
                .findUnique();

        if(dagrComponent == null) {
            result = Optional.empty();
        } else {
            result = Optional.of(dagrComponent);
        }
        return result;
    }

    @Transactional
    public boolean deleteDagrComponentByUuid(UUID componenetUuid) {
        boolean result;
        Optional<DagrComponent> dagrComponentOptional =findComponentByUuid(componenetUuid);

        if(dagrComponentOptional.isPresent()) {
            result = dagrComponentOptional.get().delete();
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Adds the given DagrComponent to the database
     *
     * @param dagrComponent
     *          The DagrComponent to be added.
     * */
    @Transactional
    public void saveComponent(DagrComponent dagrComponent) {
        dagrComponent.save();
        Logger.info("Successfully saved DAGR component with UUID: " + dagrComponent.dagrComponentUuid);
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
}
