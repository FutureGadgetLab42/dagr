package controllers.http_methods.requests;

import java.util.UUID;

public class FindDagrByUuidRequest {

    UUID dagrUuid;

    public UUID getDagrUuid() {
        return dagrUuid;
    }

    public void setDagrUuid(UUID dagrUuid) {
        this.dagrUuid = dagrUuid;
    }
}
