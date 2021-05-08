package ir.parian.loan.service.dto;

import ir.parian.loan.service.enums.SystemAccountType;
import ir.parian.loan.service.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewTransactionDto {
    private Long memberAccountId;
    private SystemAccountType systemAccountType;
    private BigDecimal amount;
    private String description;
    private TransactionType type;
}
