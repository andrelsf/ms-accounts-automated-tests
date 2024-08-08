package andrelsf.com.github.automations.dtos.requests;

import andrelsf.com.github.automations.dtos.responses.CustomerResponse;
import java.math.BigDecimal;

public record PostTransferRequest(Integer agency, Integer accountNumber, BigDecimal amount) {

  public static PostTransferRequest of(CustomerResponse customer, BigDecimal amount) {
    return new PostTransferRequest(
        customer.account().agency(),
        customer.account().accountNumber(),
        amount);
  }
}
