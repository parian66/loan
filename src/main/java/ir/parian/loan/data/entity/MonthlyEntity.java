package ir.parian.loan.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = MonthlyEntity.TABLE)
public class MonthlyEntity extends AbstractAuditable<Long> {
    public static final String TABLE = "monthly";

    @ManyToOne
    private MemberAccountEntity account;

    private BigDecimal amount;

    private Boolean paid;

    @Temporal(TemporalType.DATE)
    private Date date;
}
