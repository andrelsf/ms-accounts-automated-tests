package andrelsf.com.github.automations.dtos.responses;

public record CustomerResponse(
    String customerId,
    String name,
    String cpf,
    AccountResponse account
) {}
