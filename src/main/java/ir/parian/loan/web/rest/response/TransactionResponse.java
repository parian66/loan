package ir.parian.loan.web.rest.response;

import ir.parian.loan.service.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionResponse {
    private Date date;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private BigDecimal balance;
}
