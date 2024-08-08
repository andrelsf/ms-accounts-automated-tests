package andrelsf.com.github.automations.dtos.responses;

import java.math.BigDecimal;

public record TransferResponse(
    String transferId,
    Integer targetAgency,
    Integer targetAccountNumber,
    BigDecimal amount,
    String status,
    String message,
    String transferDate
) {}
