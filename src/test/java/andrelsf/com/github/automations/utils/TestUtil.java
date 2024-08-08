package andrelsf.com.github.automations.utils;

import static org.apache.commons.lang3.RandomStringUtils.random;

import andrelsf.com.github.automations.dtos.requests.AccountRequest;
import andrelsf.com.github.automations.dtos.requests.PostCustomerRequest;
import java.math.BigDecimal;
import java.util.UUID;

public class TestUtil {

  public final static String bobAccountId;
  public final static String aliceAccountId;
  public final static String accountIdInvalid;
  public final static BigDecimal amountAllowed;
  public final static BigDecimal amountNotAllowed;
  public final static String invalidAccountIdFormat;

  static {
    bobAccountId = "5ccac7a4-513e-11ef-9485-bbfbb6bdc7c6";
    aliceAccountId = "3d05773e-513e-11ef-85b4-938a0beed59a";
    accountIdInvalid = UUID.randomUUID().toString();
    invalidAccountIdFormat = "0ab4165471c445918697d3d620b0db2b";
    amountNotAllowed = BigDecimal.valueOf(10001.00F);
    amountAllowed = BigDecimal.valueOf(1000.00F);
  }

  public static String getCustomerIdFromURILocation(final String uriLocation) {
    final String[] data = uriLocation.split("/");
    return data[data.length - 1];
  }

  public static PostCustomerRequest buildCustomer() {
    final String cpf = random(11, false, true);
    final Integer agency = Integer.valueOf(random(4, false, true));
    final Integer accountNumber = Integer.valueOf(random(7, false, true));
    return new PostCustomerRequest(
        "Jose Nome Facil", cpf, new AccountRequest(agency, accountNumber));
  }
}
