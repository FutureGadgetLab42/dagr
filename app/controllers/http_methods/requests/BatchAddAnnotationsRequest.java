package controllers.http_methods.requests;

import java.util.List;

public class BatchAddAnnotationsRequest {

    private List<AddAnnotationRequest> addAnnotationRequests;

    public BatchAddAnnotationsRequest(List<AddAnnotationRequest> addAnnotationRequests) {
        this.addAnnotationRequests = addAnnotationRequests;
    }

    public List<AddAnnotationRequest> getAddAnnotationRequests() {
        return addAnnotationRequests;
    }

    public void setAddAnnotationRequests(List<AddAnnotationRequest> addAnnotationRequests) {
        this.addAnnotationRequests = addAnnotationRequests;
    }

}
