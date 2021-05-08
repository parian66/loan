package ir.parian.loan.service.dto;

import ir.parian.loan.service.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDto {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal balance;
    private String description;
    private Date date;
}
