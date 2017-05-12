package models.dagr.factories;

import controllers.http_methods.requests.CreateDagrRequest;
import utilities.data_sources.parser.ParsedHtmlData;
import utilities.exceptions.DagrCreationException;
import models.dagr.Dagr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        DagrBuilder dagrBuilder = new DagrBuilder();
        dagrBuilder.setDagrUuid(UUID.randomUUID());
        dagrBuilder.setDagrCreationDate(new Date());
        dagrBuilder.setContentType(request.getContentType());
        dagrBuilder.setResourceLocation(request.getResourceLocation());
        dagrBuilder.setDocumentName(request.getDocumentName());
        dagrBuilder.setDagrName(request.getDagrName());
        dagrBuilder.setLastModified(request.getLastModified());
        dagrBuilder.setDocumentCreationTime(request.getDocumentCreationTime());
        dagrBuilder.setFileSize(request.getFileSize());
        dagrBuilder.setAuthor(request.getAuthor());
        return dagrBuilder.build();
    }

    public List<Dagr> fromParsedData(List<ParsedHtmlData> parsedData) {
        List<Dagr> result = new ArrayList<>();
        for(ParsedHtmlData data : parsedData) {
            CreateDagrRequest createDagrRequest = new CreateDagrRequest();
            createDagrRequest.setContentType(data.getContentType());
            createDagrRequest.setFileSize(data.getSize());
            createDagrRequest.setResourceLocation(data.getResourceLocation());
            result.add(buildDagr(createDagrRequest));
        }
        return result;
    }

}
