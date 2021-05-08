package ir.parian.loan.web.rest.request;

import ir.parian.loan.service.enums.TransactionType;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class NewTransactionRequest {
    @NotNull
    private TransactionType type;

    @DecimalMin("1")
    private BigDecimal amount;

    private String description;
}
