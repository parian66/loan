package ir.parian.loan.data.entity;

import ir.parian.loan.service.enums.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = TransactionEntity.TABLE)
public class TransactionEntity extends AbstractAuditable<Long> {
    public static final String TABLE = "transaction";

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private BigDecimal balance;

    @ManyToOne
    private AccountEntity account;

    @Lob
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}
