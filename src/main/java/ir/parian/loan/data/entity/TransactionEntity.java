package ir.parian.loan.data.entity;

import ir.parian.loan.service.enums.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class TransactionEntity extends AbstractAuditable<Long> {
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    @ManyToOne
    private BalanceEntity balance;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String description;
}
