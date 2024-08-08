package andrelsf.com.github.automations.core;

import andrelsf.com.github.automations.utils.ApiUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;

public class RestAssuredBaseTest implements ApiUtil {

  public static RequestSpecification requestSpecification;
  public static ResponseSpecification responseSpecification;

  @BeforeClass
  public static void initialize() {
    RestAssured.baseURI = BASE_URI;
    RestAssured.basePath = BASE_PATH;
    RestAssured.port = PORT;

    requestSpecification = new RequestSpecBuilder()
        .setContentType(ContentType.JSON)
        .build();

    responseSpecification = new ResponseSpecBuilder()
        .expectResponseTime(Matchers.lessThan(MAX_TIMEOUT))
        .build();

    RestAssured.requestSpecification = requestSpecification;
    RestAssured.responseSpecification = responseSpecification;

    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }
}
