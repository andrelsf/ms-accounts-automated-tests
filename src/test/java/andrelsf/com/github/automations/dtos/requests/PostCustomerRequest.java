package andrelsf.com.github.automations.dtos.requests;

public record PostCustomerRequest(
    String name,
    String cpf,
    AccountRequest account
) {}
