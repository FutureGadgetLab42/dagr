import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import play.test.Helpers;
import play.libs.Json;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.WithApplication;

public class IntegrationTest extends WithApplication {

    public static <T> RequestBuilder fakeRequestWithJson(T input, String method, String url) {
        JsonNode jsonNode = Json.toJson(input);
        RequestBuilder fakeRequest = Helpers.fakeRequest(method, url).bodyJson(jsonNode);
        return fakeRequest;
    }

    @Before
    public void setup() {

    }

    @Test
    public void testCreateDagr() {
        Helpers.running(Helpers.fakeApplication(), () -> {
           RequestBuilder mockRequest = Helpers.fakeRequest().bodyJson()
        });
    }

    @Test
    public void testAddComponent() {

    }

    @Test
    public void testDeleteDagr() {

    }

    @Test
    public void testFindDagrByUuid() {

    }

    @Test
    public void deleteComponent() {

    }

    @Test
    public void findComponentByUuid() {

    }

    @Test
    public void testAddAnnotation() {

    }

    @Test
    public void testDeleteAnnotation() {

    }

    @Test
    public void testBatchAddAnnotations() {

    }

}
