package andrelsf.com.github.automations.suites;

import static andrelsf.com.github.automations.utils.TestUtil.accountIdInvalid;
import static andrelsf.com.github.automations.utils.TestUtil.aliceAccountId;
import static andrelsf.com.github.automations.utils.TestUtil.amountAllowed;
import static andrelsf.com.github.automations.utils.TestUtil.amountNotAllowed;
import static andrelsf.com.github.automations.utils.TestUtil.bobAccountId;
import static andrelsf.com.github.automations.utils.TestUtil.invalidAccountIdFormat;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import andrelsf.com.github.automations.core.RestAssuredBaseTest;
import andrelsf.com.github.automations.dtos.requests.PostCustomerRequest;
import andrelsf.com.github.automations.dtos.requests.PostTransferRequest;
import andrelsf.com.github.automations.dtos.responses.CustomerResponse;
import andrelsf.com.github.automations.dtos.responses.TransferResponse;
import andrelsf.com.github.automations.utils.TestUtil;
import java.util.Arrays;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApiCustomersSuiteTest extends RestAssuredBaseTest {

  private static String uriLocation;
  private static CustomerResponse bobCustomerResponse;
  private static CustomerResponse aliceCustomerResponse;
  private static PostCustomerRequest postCustomerRequest;

  @Test
  public void t001_shouldReturn_404_customerNotFoundBy_invalidId() {
    given()
        .pathParam("accountId", accountIdInvalid)
      .when()
        .get("/{accountId}")
      .then()
        .assertThat().statusCode(404)
        .assertThat().body("code", is(404))
        .assertThat().body("message", is("Customer not found by ID=".concat(accountIdInvalid)));
  }

  @Test
  public void t002_shouldReturn_400_invalidFormatCustomerId() {
    given()
        .pathParam("accountId", invalidAccountIdFormat)
        .when()
        .get("/{accountId}")
        .then()
        .assertThat().statusCode(400)
        .assertThat().body("code", is(400))
        .assertThat().body("message", is("Invalid parameter"));
  }

  @Test
  public void t003_shouldReturn_200_with_aliceAccount() {
    aliceCustomerResponse = given()
        .pathParam("accountId", aliceAccountId)
      .when()
        .get("/{accountId}")
      .then()
        .assertThat().statusCode(200)
        .assertThat().body("customerId", is(aliceAccountId))
        .assertThat().body("name", is("Alice Bar"))
        .assertThat().body("cpf", is("22233344448"))
        .assertThat().body("account.accountId", is(aliceAccountId))
        .assertThat().body("account.agency", is(1234))
        .assertThat().body("account.accountNumber", is(1000112))
        .assertThat().body("account.status", is("ACTIVE"))
        .assertThat().body("account.balance", notNullValue())
        .assertThat().body("account.createdAt", notNullValue())
        .assertThat().body("account.lastUpdated", notNullValue())
      .extract()
        .response()
        .getBody()
        .as(CustomerResponse.class);
  }

  @Test
  public void t004_shouldReturn_200_with_bobAccount() {
    bobCustomerResponse = given()
        .pathParam("accountId", bobAccountId)
        .when()
        .get("/{accountId}")
        .then()
        .assertThat().statusCode(200)
        .assertThat().body("customerId", is(bobAccountId))
        .assertThat().body("name", is("Bob Foo"))
        .assertThat().body("cpf", is("33344455587"))
        .assertThat().body("account.accountId", is(bobAccountId))
        .assertThat().body("account.agency", is(1234))
        .assertThat().body("account.accountNumber", is(1000223))
        .assertThat().body("account.status", is("ACTIVE"))
        .assertThat().body("account.balance", notNullValue())
        .assertThat().body("account.createdAt", notNullValue())
        .assertThat().body("account.lastUpdated", notNullValue())
        .extract()
        .response()
        .getBody()
        .as(CustomerResponse.class);
  }

  @Test
  public void t005_doTransfer_shouldReturn_422_withAmountNotAllowed() {
    final String sourceAccountId = bobCustomerResponse.account().accountId();
    final PostTransferRequest postTransferRequest = PostTransferRequest.of(aliceCustomerResponse, amountNotAllowed);
    given()
        .pathParam("accountId", sourceAccountId)
        .body(postTransferRequest)
        .when()
        .post("/{accountId}/transfers")
        .then()
        .assertThat().statusCode(422)
        .assertThat().body("transferId", notNullValue())
        .assertThat().body("targetAgency", is(postTransferRequest.agency()))
        .assertThat().body("targetAccountNumber", is(postTransferRequest.accountNumber()))
        .assertThat().body("amount", is(10001.00F))
        .assertThat().body("status", is("FAILED"))
        .assertThat().body("message", is("Transfer amount not allowed"))
        .assertThat().body("transferDate", notNullValue())
        .log();
  }

  @Test
  public void t006_doTransfer_shouldReturn_200_withTransferStatus_COMPLETED() {
    final String sourceAccountId = bobCustomerResponse.account().accountId();
    final PostTransferRequest postTransferRequest = PostTransferRequest.of(aliceCustomerResponse, amountAllowed);
    given()
        .pathParam("accountId", sourceAccountId)
        .body(postTransferRequest)
        .when()
        .post("/{accountId}/transfers")
        .then()
        .assertThat().statusCode(200)
        .assertThat().body("transferId", notNullValue())
        .assertThat().body("targetAgency", is(postTransferRequest.agency()))
        .assertThat().body("targetAccountNumber", is(postTransferRequest.accountNumber()))
        .assertThat().body("amount", is(1000.00F))
        .assertThat().body("status", is("COMPLETED"))
        .assertThat().body("message", is("Transfer completed successfully."))
        .assertThat().body("transferDate", notNullValue())
        .log();
  }

  @Test
  public void t007_registerANewClient_shouldReturn_201_withHeaderLocation() {
    postCustomerRequest = TestUtil.buildCustomer();
    uriLocation = given()
        .body(postCustomerRequest)
        .when()
        .post()
        .then()
        .assertThat().statusCode(201)
        .assertThat().header("Location", notNullValue())
        .extract()
        .header("Location");
  }

  @Test
  public void t008_tryRegisterSameClient_shouldReturn_409_conflict() {
    given()
        .body(postCustomerRequest)
        .when()
        .post()
        .then()
        .assertThat().statusCode(409)
        .assertThat().body("code", is(409))
        .assertThat().body("message", is("CPF already registered"));
  }

  @Test
  public void t009_getANewCustomerRegistered_shouldReturn_200_withCustomerAccountDetails() {
    final String newCustomerId = TestUtil.getCustomerIdFromURILocation(uriLocation);
    given()
        .pathParam("accountId", newCustomerId)
        .when()
        .get("/{accountId}")
        .then()
        .assertThat().statusCode(200)
        .assertThat().body("customerId", is(newCustomerId))
        .assertThat().body("name", is("Jose Nome Facil"))
        .assertThat().body("cpf", is(postCustomerRequest.cpf()))
        .assertThat().body("account.accountId", is(newCustomerId))
        .assertThat().body("account.agency", is(postCustomerRequest.account().agency()))
        .assertThat().body("account.accountNumber", is(postCustomerRequest.account().accountNumber()))
        .assertThat().body("account.status", is("ACTIVE"))
        .assertThat().body("account.balance", notNullValue())
        .assertThat().body("account.createdAt", notNullValue())
        .assertThat().body("account.lastUpdated", notNullValue());
  }

  @Test
  public void t010_GETAllTransfers_fromBobAccount_shouldReturn_200_withListGreaterThan_2() {
    final TransferResponse[] transfers = given()
        .pathParam("accountId", bobAccountId)
        .when()
        .get("/{accountId}/transfers")
        .then()
        .assertThat().statusCode(200)
        .assertThat().body("size()", greaterThanOrEqualTo(2))
        .extract()
        .as(TransferResponse[].class);

    Arrays.stream(transfers).forEach(transferResponse -> {
      assertThat(transferResponse)
          .isNotNull()
          .isInstanceOf(TransferResponse.class);
      assertThat(transferResponse.status())
          .isNotBlank()
          .containsAnyOf("COMPLETED", "FAILED");
    });
  }

  @Test
  public void t011_InactivateCustomerByID_shouldReturn_204_noContent() {
    final String newCustomerId = TestUtil.getCustomerIdFromURILocation(uriLocation);
    given()
        .pathParam("accountId", newCustomerId)
        .when()
        .delete("/{accountId}")
        .then()
        .assertThat().statusCode(204);
  }
}
